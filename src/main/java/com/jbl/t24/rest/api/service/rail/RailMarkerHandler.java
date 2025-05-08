package com.jbl.t24.rest.api.service.rail;

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
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.constant.JwtErrorsCBS;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.enums.utils.ErrorMessageGenerator;
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

    Logger logger = LogManager.getLogger(RailMarkerHandler.class);

    public ResponseEntity<?> handleMarker(String requestOFS, RailMarkerAccount railMarker)
			throws Exception {

        logger.info("Ft Handle Service Started for ID :: {}" + railMarker.getRailMarkerId());
        logger.info("Ofs Request:  {}" +requestOFS);

        TccUtility tccUtility = new TccUtility(transactionChannel);

        String responseData = tccUtility.sendRequest(requestOFS);
		System.out.println(responseData);

        
        if (responseData.startsWith("40")) {

			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);
		
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
		
				logger.error("Jboss Server error----for ::{}",railMarker.getRailMarkerId() + "::\n" + jwtErrorResponse);
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
            }
            
            // String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());

            return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());
        }

    }

}
