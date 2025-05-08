package com.jbl.t24.rest.api.service.rail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.FtTxResponseRail;
import com.jbl.t24.rest.api.common.model.FtTxResponseRail.FtTxResponseRailBuilder;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.config.Mapper;
import com.jbl.t24.rest.api.constant.JwtErrorsCBS;
import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.enums.utils.ErrorMessageGenerator;
import com.jbl.t24.rest.api.model.rail.RailFundTransfer;
import com.jbl.t24.rest.api.model.rail.RailLockAccount;
import com.jbl.t24.rest.api.model.rail.RailUnlockAccount;
import com.jbl.t24.rest.api.service.rtgs.ResponseMessageProcessor;
import com.jbl.t24.rest.api.tccUtility.TccUtility;


@Service
public class RailServiceHandler {

    @Value("${channel.name.trx}")
	private String transactionChannel;

    @Autowired
	ResponseMessageProcessor processor = new ResponseMessageProcessor();

    Logger logger = LogManager.getLogger(RailServiceHandler.class);
    public ResponseEntity<?> handleFtTransaction(String requestOFS, RailFundTransfer railFundTransfer)
			throws Exception {

        logger.info("Ft Handle Service Started for ID :: {}" + railFundTransfer.getUniqueftId());
        logger.info("Ofs Request:  {}" +requestOFS);

        TccUtility tccUtility = new TccUtility(transactionChannel);

        // String markerResponseData = tccUtility.sendRequest(markerRequestOFS);
        // System.out.println("Marker Request: "+ markerRequestOFS+"\n");
        // System.out.println("Marker Response: "+ markerResponseData);

        String responseData = tccUtility.sendRequest(requestOFS);
		System.out.println(responseData);

        
        if (responseData.startsWith("40")) {

			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);
		
			logger.error("Jboss Server Error for :: {}",railFundTransfer.getUniqueftId() + "::\n"+ jwtErrorResponse);

			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} 
        else {
            FtTxResponseRailBuilder ftResponse = FtTxResponseRail.builder();

            ftResponse.status(HttpStatus.OK)
                    .uniqueftId(railFundTransfer.getUniqueftId());

            ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();

            try {
                wrapper = processor.handleResponseOfs(responseData, 0);
                ftResponse.ftRef(wrapper.getFtRef())
                        .message(wrapper.getMessage())
                        .responseCode(wrapper.getResponseCode())
                        .additionalInfo(wrapper.getAdditionalInfo())
                        .timestamp(wrapper.getCbsHittingTime());
            } catch (Exception e) {
                
                JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
						ErrorMessageGenerator.getErrorMessage(e), ResponseStatus.FIVEZ0.getValue());
		
				logger.error("Jboss Server error----for ::{}",railFundTransfer.getUniqueftId() + "::\n" + jwtErrorResponse);
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
            }
            
            // String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());

            return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());
        }


    }

    public ResponseEntity<?> handleLock(String requestOFS, RailLockAccount railLockAccount)
			throws Exception {

        logger.info("Ft Handle Service Started for ID :: {}" + railLockAccount.getAtUniqueId());
        logger.info("Ofs Request:  {}" +requestOFS);

        TccUtility tccUtility = new TccUtility(transactionChannel);

        String responseData = tccUtility.sendRequest(requestOFS);
		System.out.println(responseData);

        
        if (responseData.startsWith("40")) {

			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);
		
			logger.error("Jboss Server Error for :: {}",railLockAccount.getAtUniqueId() + "::\n"+ jwtErrorResponse);

			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} 
        else {
            FtTxResponseRailBuilder ftResponse = FtTxResponseRail.builder();

            ftResponse.status(HttpStatus.OK)
                    .uniqueftId(railLockAccount.getAtUniqueId());

            ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();

            try {
                wrapper = processor.handleResponseOfs(responseData, 0);
                ftResponse.ftRef(wrapper.getFtRef())
                        .message(wrapper.getMessage())
                        .responseCode(wrapper.getResponseCode())
                        .additionalInfo(wrapper.getAdditionalInfo())
                        .timestamp(wrapper.getCbsHittingTime());
            } catch (Exception e) {
                
                JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
						ErrorMessageGenerator.getErrorMessage(e), ResponseStatus.FIVEZ0.getValue());
		
				logger.error("Jboss Server error----for ::{}",railLockAccount.getAtUniqueId() + "::\n" + jwtErrorResponse);
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
            }
            
            // String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());

            return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());
        }

    }

    public ResponseEntity<?> handleUnLock(String requestOFS, RailUnlockAccount railUnLockAccount)
			throws Exception {

        logger.info("Ft Handle Service Started for ID :: {}" + railUnLockAccount.getAtUniqueId());
        logger.info("Ofs Request:  {}" +requestOFS);

        TccUtility tccUtility = new TccUtility(transactionChannel);

        String responseData = tccUtility.sendRequest(requestOFS);
		System.out.println(responseData);

        
        if (responseData.startsWith("40")) {

			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);
		
			logger.error("Jboss Server Error for :: {}",railUnLockAccount.getAtUniqueId() + "::\n"+ jwtErrorResponse);

			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} 
        else {
            FtTxResponseRailBuilder ftResponse = FtTxResponseRail.builder();

            ftResponse.status(HttpStatus.OK)
                    .uniqueftId(railUnLockAccount.getAtUniqueId());

            ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();

            try {
                wrapper = processor.handleResponseOfs(responseData, 0);
                ftResponse.ftRef(wrapper.getFtRef())
                        .message(wrapper.getMessage())
                        .responseCode(wrapper.getResponseCode())
                        .additionalInfo(wrapper.getAdditionalInfo())
                        .timestamp(wrapper.getCbsHittingTime());
            } catch (Exception e) {
                
                JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
						ErrorMessageGenerator.getErrorMessage(e), ResponseStatus.FIVEZ0.getValue());
		
				logger.error("Jboss Server error----for ::{}",railUnLockAccount.getAtUniqueId() + "::\n" + jwtErrorResponse);
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
            }
            
            // String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());

            return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());
        }

    }
    
}
