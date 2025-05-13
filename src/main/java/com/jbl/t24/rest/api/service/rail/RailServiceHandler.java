package com.jbl.t24.rest.api.service.rail;

import java.sql.SQLException;

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
import com.jbl.t24.rest.api.exception.TransactionSaveException;
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

    @Autowired
    RailInfoService railInfoService;

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

        RailLockAccount railLockAccountosave= new RailLockAccount();

        railLockAccountosave.setAtUniqueId(railLockAccount.getAtUniqueId());
        railLockAccountosave.setAccountToLock(railLockAccount.getAccountToLock());
        railLockAccountosave.setCoCode(railLockAccount.getCoCode());
        railLockAccountosave.setLockAmount(railLockAccount.getLockAmount());
        railLockAccountosave.setLockDetails(railLockAccount.getLockDetails());
        railLockAccountosave.setLockStartDate(railLockAccount.getLockStartDate());
        railLockAccountosave.setLockEndDate(railLockAccount.getLockEndDate());
        railLockAccountosave.setIp(railLockAccount.getIp());
        railLockAccountosave.setHostname(railLockAccount.getHostname());
    

        TccUtility tccUtility = new TccUtility(transactionChannel);

        String responseData = tccUtility.sendRequest(requestOFS);
		System.out.println(responseData);

        railLockAccountosave.setOfsRequest(requestOFS);
        railInfoService.saveRailLockAccount(railLockAccountosave);
        
        if (responseData.startsWith("40")) {

			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);

            railLockAccountosave.setStatus(FtStatus.FAILED.getValue());
            railLockAccountosave.setOfsResponse(responseData);
            railLockAccountosave.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));

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

                railLockAccountosave.setStatus(FtStatus.FAILED.getValue());
				railLockAccountosave.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
				railLockAccountosave.setOfsResponse(responseData);

				railInfoService.saveRailLockAccount(railLockAccountosave);

				logger.error("Jboss Server error----for ::{}",railLockAccount.getAtUniqueId() + "::\n" + jwtErrorResponse);
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
            }
            
             String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());

                railLockAccountosave.setStatus(wrapper.getFtStatus());
                railLockAccountosave.setFtResponseStr(ftResponseStr);
				railLockAccountosave.setOfsResponse(responseData);

                 try {
                    railLockAccountosave.setOfsResponse(responseData);
                    railInfoService.saveRailLockAccount(railLockAccountosave);
                } catch (Exception e) {
                    // TODO: handle exception
                    railLockAccountosave.setStatus(FtStatus.FAILED.getValue());
				railLockAccountosave.setOfsResponse("");

				railInfoService.saveRailLockAccount(railLockAccountosave);

				logger.info("-----::ERROR CHECKING----:: {} {}", railLockAccountosave.getRailLockId());
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

            return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());
        }

    }

    public ResponseEntity<?> handleUnLock(String requestOFS, RailUnlockAccount railUnLockAccount)
			throws Exception {

        logger.info("Ft Handle Service Started for ID :: {}" + railUnLockAccount.getAtUniqueId());
        logger.info("Ofs Request:  {}" +requestOFS);

        RailUnlockAccount unLockAccountosave= new RailUnlockAccount();

        unLockAccountosave.setAtUniqueId(railUnLockAccount.getAtUniqueId());
        unLockAccountosave.setCoCode(railUnLockAccount.getCoCode());
        unLockAccountosave.setLockRefId(railUnLockAccount.getLockRefId());
        unLockAccountosave.setIp(railUnLockAccount.getIp());
        unLockAccountosave.setHostname(railUnLockAccount.getHostname());

        TccUtility tccUtility = new TccUtility(transactionChannel);

        String responseData = tccUtility.sendRequest(requestOFS);
		System.out.println(responseData);

        unLockAccountosave.setOfsRequest(requestOFS);
        railInfoService.saveRailUnlockAccount(unLockAccountosave);


        if (responseData.startsWith("40")) {

			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);

            unLockAccountosave.setStatus(FtStatus.FAILED.getValue());
            unLockAccountosave.setOfsResponse(responseData);
            unLockAccountosave.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
            unLockAccountosave.setIp(railUnLockAccount.getIp());
            unLockAccountosave.setHostname(railUnLockAccount.getHostname());

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
		
                unLockAccountosave.setStatus(FtStatus.FAILED.getValue());
				unLockAccountosave.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
				unLockAccountosave.setOfsResponse(responseData);

				railInfoService.saveRailUnlockAccount(unLockAccountosave);
				logger.error("Jboss Server error----for ::{}",railUnLockAccount.getAtUniqueId() + "::\n" + jwtErrorResponse);
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
            }

             String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());

                unLockAccountosave.setStatus(wrapper.getFtStatus());
                unLockAccountosave.setFtResponseStr(ftResponseStr);
				unLockAccountosave.setOfsResponse(responseData);

                try {
                    unLockAccountosave.setOfsResponse(responseData);
                    railInfoService.saveRailUnlockAccount(unLockAccountosave);
                } catch (Exception e) {
                    // TODO: handle exception
                    unLockAccountosave.setStatus(FtStatus.FAILED.getValue());
				unLockAccountosave.setOfsResponse("");

				railInfoService.saveRailUnlockAccount(unLockAccountosave);

				logger.info("-----::ERROR CHECKING----:: {} {}", railUnLockAccount.getAtUniqueId());
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

            return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());
        }

    }
    
}
