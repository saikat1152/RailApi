package com.jbl.t24.rest.api.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbl.t24.rest.api.common.model.FtResponse;
import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.common.model.FtTxResponse.FtTxResponseBuilder;
import com.jbl.t24.rest.api.config.Mapper;

import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.CommonFtInfo;
import com.jbl.t24.rest.api.model.EftInfoOutward;
import com.jbl.t24.rest.api.model.RtgsInfoInward;
import com.jbl.t24.rest.api.model.RtgsInfoOutward;
import com.jbl.t24.rest.api.model.RtgsInfoPacsNineInward;
import com.jbl.t24.rest.api.model.RtgsInfoPacsNineOutward;
import com.jbl.t24.rest.api.model.SettlementInInfo;
import com.jbl.t24.rest.api.model.SettlementOutInfo;
import com.jbl.t24.rest.api.tccUtility.TccUtility;
import java.sql.Timestamp;

@Service
public class FtHandlerServiceN {
    Logger logger = LogManager.getLogger(FtHandlerService.class);

    @Autowired
    RtgsInfoService service;

    @Autowired
    ResponseMessageProcessor processor = new ResponseMessageProcessor();

    public ResponseEntity<?> handleFtTransaction(String requestOFS, CommonFtInfo ftInfo,
            String uniqueId)
            throws Exception {

        logger.info("Ft Handle Service Started");
        CommonFtInfo ftSave;
        CommonFtInfo ftExist;

        if (ftInfo instanceof RtgsInfoInward) {
            ftSave = new RtgsInfoInward();
            ftExist = new RtgsInfoInward();
        } else if (ftInfo instanceof RtgsInfoOutward) {
            ftSave = new RtgsInfoOutward();
            ftExist = new RtgsInfoOutward();
        } else if(ftInfo instanceof RtgsInfoPacsNineInward){
            ftSave = new RtgsInfoPacsNineInward();
            ftExist = new RtgsInfoPacsNineInward();
        }else if(ftInfo instanceof RtgsInfoPacsNineOutward){
        	ftSave = new RtgsInfoPacsNineOutward();
            ftExist = new RtgsInfoPacsNineOutward();
        } else if (ftInfo instanceof SettlementInInfo) {
            ftSave = new SettlementInInfo();
            ftExist = new SettlementInInfo();
        } else if (ftInfo instanceof SettlementOutInfo) {
            ftSave = new SettlementOutInfo();
            ftExist = new SettlementOutInfo();
        }

        TccUtility tccUtility = new TccUtility();

        ftSave = ftInfo;
        ftExist = service
                .findByUniqueId(ftInfo, uniqueId);

        int statusExist = ftExist == null ? 0 : ftExist.getStatus();

        if (ftExist != null) {

            if (statusExist == FtStatus.SUCCESS.getValue() || statusExist == FtStatus.REVERSED.getValue()) {

                String ftResponseStr = ftExist.getFtResponseStr();
                // ObjectMapper mapper = new ObjectMapper();
                // FtTxResponse savedFtResponse = mapper.readValue(ftResponseStr,
                // FtTxResponse.class);
                FtTxResponse ftResponse = Mapper.readValue(ftResponseStr);

                String message = ftExist.getStatus() == 2 ? ResponseStatus.TWOZ2.getText()
                        : ResponseStatus.TWOZ6.getText();

                ftResponse.setResponseCode(ResponseStatus.TWOZ2.getValue());
                ftResponse.setMessage(message);
                return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
            }

            else {
                logger.info("FT Alreeady Exists but Failed or Pending");
                ftSave = ftExist;
                ftSave.setCoCode(ftInfo.getCoCode());

            }
        }

        System.out.println(ftSave.toString());

        ftSave.setStatus(FtStatus.PENDING.getValue());
        ftSave.setOfsRequest(requestOFS);
        service.save(ftSave);
        logger.info("FT saved as PENDING");

        String responseData = tccUtility.sendRequest(requestOFS);

        ftSave.setIssueDate(new Timestamp(System.currentTimeMillis()));

        if (responseData.equals("400") || responseData.equals("401") || responseData.equals("402")
                || responseData.equals("403") || responseData.equals("405")) {
        	int responseCode;
        	String responseMsg;
        	if(responseData.equals("405")) {
        		responseCode = ResponseStatus.FOURZ27.getValue();
        		responseMsg = ResponseStatus.FOURZ27.getText();
        	}
        	else {
        		responseCode = ResponseStatus.FIVEZ3.getValue();
        		responseMsg = ResponseStatus.FIVEZ3.getText();
        	}

            JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, responseMsg,responseCode);
            service.save(ftSave);

            logger.error("Server Error:: " + jwtErrorResponse);

            return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);

        } else {
            FtTxResponseBuilder ftResponse = FtTxResponse.builder();

            ftResponse.status(HttpStatus.OK)
                    .uniqueEft(uniqueId);

            ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
            wrapper = processor.handleResponseOfs(responseData,0);

            ftResponse.ftRef(wrapper.getFtRef())
                    .message(wrapper.getMessage())
                    .responseCode(wrapper.getResponseCode())
                    .additionalInfo(wrapper.getAdditionalInfo())
                    .timestamp(ftSave.getIssueDate());

            String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());

            ftSave.setStatus(wrapper.getFtStatus());
            ftSave.setFtResponseStr(ftResponseStr);
            ftSave.setCbsFtno(ftResponse.build().getFtRef());
            ftSave.setOfsResponse(responseData);

            try {

                service.save(ftSave);
            } catch (Exception e) {
                logger.info("-----::ERROR CHECKING----::" + ftSave);
                logger.error(e.getMessage(), e);
                logger.info("JPA Error Occured During Save: " + e.getMessage());
            }

            logger.info("FT Data Saved");
            logger.info("Ft Handle Service Finished");

            return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

        }

    }

}