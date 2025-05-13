package com.jbl.t24.rest.api.service.rail;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.common.model.FtMarkerResponse;
import com.jbl.t24.rest.api.common.model.FtMarkerResponse.FtMarkerResponseBuilder;
import com.jbl.t24.rest.api.common.model.FtTxResponseRail;
import com.jbl.t24.rest.api.common.model.FtTxResponseRail.FtTxResponseRailBuilder;
import com.jbl.t24.rest.api.config.Mapper;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.constant.JwtErrorsCBS;
import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.enums.utils.ErrorMessageGenerator;
import com.jbl.t24.rest.api.exception.TransactionSaveException;
import com.jbl.t24.rest.api.model.rail.RailMarkerAccount;
import com.jbl.t24.rest.api.model.rail.RailUnlockAccount;
import com.jbl.t24.rest.api.service.rtgs.ResponseMessageProcessor;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@Service
public class RailMarkerHandler {

    @Value("${channel.name.trx}")
	private String transactionChannel;

    @Autowired
	ResponseMessageProcessor processor = new ResponseMessageProcessor();

    @Autowired
    RailInfoService railInfoService;

    Logger logger = LogManager.getLogger(RailMarkerHandler.class);

    public ResponseEntity<?> handleMarker(String requestOFS, RailMarkerAccount railMarker)
			throws Exception {

        logger.info("Ft Handle Service Started for ID :: {}" + railMarker.getRailMarkerId());
        logger.info("Ofs Request:  {}" +requestOFS);

        RailMarkerAccount railMarkerAccount = new RailMarkerAccount();

        railMarkerAccount.setAccountToMark(railMarker.getAccountToMark());
        railMarkerAccount.setMarkerType(railMarker.getMarkerType());
        railMarkerAccount.setCoCode(railMarker.getCoCode());
        railMarkerAccount.setIp(railMarker.getIp());
        railMarkerAccount.setHostname(railMarker.getHostname());

        TccUtility tccUtility = new TccUtility(transactionChannel);

        String responseData = tccUtility.sendRequest(requestOFS);
		System.out.println(responseData);

        railMarkerAccount.setOfsRequest(requestOFS);
        railInfoService.saveRailMarkerAccount(railMarkerAccount);

        if (responseData.startsWith("40")) {

			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);
			
            railMarkerAccount.setStatus(FtStatus.FAILED.getValue());
            railMarkerAccount.setOfsResponse(responseData);
            railMarkerAccount.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
            railMarkerAccount.setIp(railMarker.getIp());
            railMarkerAccount.setHostname(railMarker.getHostname());

            logger.error("Jboss Server Error for :: {}", railMarker.getRailMarkerId() + "::\n"+ jwtErrorResponse);

			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} 
        else {
            // FtMarkerResponseBuilder ftResponse = FtMarkerResponse.builder();
            FtMarkerResponseBuilder ftResponse = FtMarkerResponse.builder();

            ftResponse.status(HttpStatus.OK);

            ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();

            try {
                wrapper = processor.handleResponseOfs(responseData, 0);
                ftResponse.message(wrapper.getMessage())
                        .responseCode(wrapper.getResponseCode())
                        .accountNumber(wrapper.getFtRef())
                        .additionalInfo(wrapper.getAdditionalInfo())
                        .timestamp(wrapper.getCbsHittingTime());
            } catch (Exception e) {
                
                JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
						ErrorMessageGenerator.getErrorMessage(e), ResponseStatus.FIVEZ0.getValue());

                railMarkerAccount.setStatus(FtStatus.FAILED.getValue());
				railMarkerAccount.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
				railMarkerAccount.setOfsResponse(responseData);

				railInfoService.saveRailMarkerAccount(railMarkerAccount);

				logger.error("Jboss Server error----for ::{}",railMarker.getRailMarkerId() + "::\n" + jwtErrorResponse);
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
            }
            
             String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());

                railMarkerAccount.setStatus(wrapper.getFtStatus());
                railMarkerAccount.setFtResponseStr(ftResponseStr);
				railMarkerAccount.setOfsResponse(responseData);

                try {
                    railMarkerAccount.setOfsResponse(responseData);
                    railInfoService.saveRailMarkerAccount(railMarkerAccount);
                } catch (Exception e) {
                    // TODO: handle exception
                    railMarkerAccount.setStatus(FtStatus.FAILED.getValue());
				railMarkerAccount.setOfsResponse("");

				railInfoService.saveRailMarkerAccount(railMarkerAccount);

				logger.info("-----::ERROR CHECKING----:: {} {}", railMarker.getRailMarkerId());
				logger.error(e.getMessage(), e);
				logger.info("JPA Error Occured During Save: " + e.getMessage());

				Throwable rootCause = e.getCause().getCause();
				if (rootCause instanceof SQLException) {
					SQLException hibernateEx = (SQLException) rootCause;

					throw new TransactionSaveException(
							"Data integrity violation",
							hibernateEx.getMessage(),
							hibernateEx.getSQLState(),
							hibernateEx.getMessage());
				}

				throw new TransactionSaveException(
						"Unexpected database error",
						null,
						null,
						e.getMessage());
			}

			logger.info("Rail Marker Data Saved for ::{} with category :: {}", railMarker.getRailMarkerId());
			logger.info("Rail Marker Handler Service Finished for ID ::{}");

            return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());
            
        }

    }

}

