package com.jbl.t24.rest.api.service.rtgs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.common.model.FtTxResponse.FtTxResponseBuilder;
import com.jbl.t24.rest.api.config.Mapper;
import com.jbl.t24.rest.api.constant.JwtErrorsCBS;
import com.jbl.t24.rest.api.constant.RtgsUniqueIdTransactionCheck;
import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.RTGSCategory;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.rtgs.CommonFtInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementInInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementOutInfo;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoInward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoOutward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineInward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineOutward;
import com.jbl.t24.rest.api.tccUtility.TccUtility;
import java.sql.Timestamp;
import java.util.Map;

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
        } else if(ftInfo instanceof RtgsInfoPacsNineOutward){
        	ftSave = new RtgsInfoPacsNineOutward();
            ftExist = new RtgsInfoPacsNineOutward();
        } else if (ftInfo instanceof RTGSSettlementInInfo) {
            ftSave = new RTGSSettlementInInfo();
            ftExist = new RTGSSettlementInInfo();
        } else if (ftInfo instanceof RTGSSettlementOutInfo) {
            ftSave = new RTGSSettlementOutInfo();
            ftExist = new RTGSSettlementOutInfo();
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
					logger.info("FT Alreeady Exists but Pending-Transaction Is On Already Processing");
					JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
							ResponseStatus.FIVEZ27.getText(), ResponseStatus.FIVEZ27.getValue());
					return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
				}

				logger.info("FT Alreeady Exists but Failed");

				String uniqueIdTransactioncheck = RtgsUniqueIdTransactionCheck.checkTransactionByUniqueId(uniqueId);

				if ((uniqueIdTransactioncheck.startsWith("40") && !uniqueIdTransactioncheck.equals("407"))
                        || uniqueIdTransactioncheck.equals("499")) {

					ftSave = ftExist;
					JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(uniqueIdTransactioncheck);

					ftSave.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
					ftSave.setOfsResponse(uniqueIdTransactioncheck);

					service.save(ftSave);

					logger.error("Server Error:: " + jwtErrorResponse);

					return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);

				} else if (uniqueIdTransactioncheck.contains("cbsFtNumber")) {

					Map<String, String> cbsSuccessTrData = Mapper.readValueForMap(uniqueIdTransactioncheck);
					ftSave = ftExist;

					FtTxResponseBuilder ftResponse = FtTxResponse.builder();

					ftResponse.status(HttpStatus.OK).uniqueEft(uniqueId).ftRef(cbsSuccessTrData.get("cbsFtNumber"))
							.message(cbsSuccessTrData.get("message"))
							.responseCode(Integer.parseInt(cbsSuccessTrData.get("responseCode")))
							.timestamp(ftInfo.getIssueDate());

					String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());

					ftSave.setStatus(2);
					ftSave.setFtResponseStr(ftResponseStr);
					ftSave.setCbsFtno(cbsSuccessTrData.get("cbsFtNumber"));
					ftSave.setOfsResponse(cbsSuccessTrData.get("ofsResponse"));
					ftSave.setDebitAmount(Double.parseDouble(cbsSuccessTrData.get("ammount")));

					service.save(ftSave);

					logger.info("FT Transaction Data Saved");
					logger.info("Ft Handle Service Finished");

					return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

				}

				ftSave = ftExist;
				ftSave.setCoCode(ftInfo.getCoCode());
				ftSave.setIssueDate(ftInfo.getIssueDate());

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

            JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);
            ftSave.setStatus(FtStatus.FAILED.getValue());
            ftSave.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
            ftSave.setOfsResponse(responseData);
            service.save(ftSave);

            logger.error("Server Error:: " + jwtErrorResponse);

            return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
        } else {
            FtTxResponseBuilder ftResponse = FtTxResponse.builder();

            ftResponse.status(HttpStatus.OK)
                    .uniqueEft(uniqueId);

            ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();

            try {
            	wrapper = processor.handleResponseOfs(responseData,0);
				ftResponse.ftRef(wrapper.getFtRef())
				        .message(wrapper.getMessage())
				        .responseCode(wrapper.getResponseCode())
				        .additionalInfo(wrapper.getAdditionalInfo())
				        .timestamp(ftSave.getIssueDate());
			} catch (Exception e1) {

				JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ0.getText(),
						ResponseStatus.FIVEZ0.getValue());
				ftSave.setStatus(FtStatus.FAILED.getValue());
				ftSave.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
				ftSave.setOfsResponse(responseData);

				logger.error("Server error----: " + jwtErrorResponse);
				service.save(ftSave);
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
			}

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