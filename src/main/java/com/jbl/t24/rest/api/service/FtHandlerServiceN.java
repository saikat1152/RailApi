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
import com.jbl.t24.rest.api.enums.RTGSCategory;
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

    public static final int RTGS_OUWARD_PACS8_MIN_VALUE = 100000;

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
            RtgsInfoOutward temp = (RtgsInfoOutward)ftInfo;
            ftSave = (RtgsInfoOutward)ftSave;
            /*
    		 * checking the categorty RTGS Ouward=1 Customs E payment=3, RTGS Inward=2
    		 */

    		int category = temp == null ? 0 : temp.getCategory();

    		if (category == RTGSCategory.RTGSOUTWARDPACS8.getValue()
    				&& ftInfo.getDebitAmount() < RTGS_OUWARD_PACS8_MIN_VALUE) {
    			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FOURZ26.getText(),
    					ResponseStatus.FOURZ26.getValue());
    			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
    		}

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

                FtTxResponse ftResponse = Mapper.readValue(ftResponseStr);

                String message = ftExist.getStatus() == 2 ? ResponseStatus.TWOZ2.getText()
                        : ResponseStatus.TWOZ6.getText();

                ftResponse.setResponseCode(ResponseStatus.TWOZ2.getValue());
                ftResponse.setMessage(message);
                return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
            }

			else {
				logger.info("FT Alreeady Exists but Failed or Pending");

				if (statusExist == FtStatus.PENDING.getValue()) {
					JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
							ResponseStatus.FOURZ27.getText(), ResponseStatus.FOURZ27.getValue());
					return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
				}
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
        System.out.println(responseData);

        ftSave.setIssueDate(new Timestamp(System.currentTimeMillis()));

        if (responseData.startsWith("40")) {

            JwtErrorResponse jwtErrorResponse = getCbsJwtError(responseData);
            ftSave.setStatus(FtStatus.FAILED.getValue());
            ftSave.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
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

    private JwtErrorResponse getCbsJwtError(String responseData) {
		JwtErrorResponse jwtErrorResponse;
		switch (responseData) {
		case "400":
		case "401":
			jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ3.getText(),
					ResponseStatus.FIVEZ3.getValue());
			break;
		case "402":
            jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ2.getText(),
                    ResponseStatus.FIVEZ2.getValue());
            break;
		case "403":
            jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ0.getText(),
                    ResponseStatus.FIVEZ0.getValue());
            break;
		case "404":
            jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ4.getText(),
                    ResponseStatus.FIVEZ4.getValue());
            break;
        case "405":
            jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ5.getText(),
                    ResponseStatus.FIVEZ5.getValue());
            break;
		default:
			jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ99.getText(),
					ResponseStatus.FIVEZ99.getValue());
		}
		return jwtErrorResponse;

    }

}