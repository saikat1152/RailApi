package com.jbl.t24.rest.api.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbl.t24.rest.api.common.model.FtResponse;
import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.config.Mapper;
import com.jbl.t24.rest.api.common.model.FtTxResponse.FtTxResponseBuilder;
import com.jbl.t24.rest.api.custom.exception.BlankOfsResponseException;
import com.jbl.t24.rest.api.enums.CBSResponseStr;
import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.RTGSCategory;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.EftInfoOutward;
import com.jbl.t24.rest.api.model.RtgsInfoInward;
import com.jbl.t24.rest.api.model.RtgsInfoOutward;
import com.jbl.t24.rest.api.model.RtgsInfoPacsNineInward;
import com.jbl.t24.rest.api.model.RtgsInfoPacsNineOutward;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@Service
public class FtHandlerService {

	Logger logger = LogManager.getLogger(FtHandlerService.class);

	@Autowired
	ResponseMessageProcessor processor = new ResponseMessageProcessor();

	@Autowired
	RtgsInfoOutwardService rtgsInfoOutwardService;

	@Autowired
	RtgsInfoInwardService rtgsInfoInwardService;

	@Autowired
	RtgsInfoPacsNineOutwardService rtgsInfoPacsNineOutwardService;

	@Autowired
	RtgsInfoPacsNineInwardService rtgsInfoPacsNineInwardService;

	public static final int RTGS_OUWARD_PACS8_MIN_VALUE = 100000;

	// @Async
	public String handleFtTransaction(String requestOFS, String channelName) {

		try {
			TccUtility tccUtility = new TccUtility(channelName);
			String ofsResponse = tccUtility.sendRequest(requestOFS);
			String[] spiltDataOfs = ofsResponse.split(",");
			return ofsResponse;
		} catch (Exception e) {
			// return ResponseEntity.status(HttpStatus.OK).body("eror");
			return "ofsResponse error";
		}

	}

	public ResponseEntity<?> handleFtTransaction(String requestOFS, EftInfoOutward eftInfoOutward,
			HttpServletRequest httpServletRequest) throws Exception {
		return null;
	}

	/*
	 * ---------------------------- RTGS Outward Transaction Pacs 08 start
	 * --------------------------------------------
	 */

	public ResponseEntity<?> handleRtgsOutwardTransaction(String requestOFS, RtgsInfoOutward rtgsInfoOutward,
			HttpServletRequest httpServletRequest) throws Exception {

		System.out.println(requestOFS);

		TccUtility tccUtility = new TccUtility();

		RtgsInfoOutward rtgsInfoExist = new RtgsInfoOutward();
//         RtgsInfoOutward rtgsInfoSave = new RtgsInfoOutward();
		RtgsInfoOutward rtgsInfoSave = rtgsInfoOutward;
		rtgsInfoExist = rtgsInfoOutwardService.findByUniqueOutwardRtgsId(rtgsInfoOutward.getUniqueOutwardRtgsId());
		int statusExist = rtgsInfoExist == null ? 0 : rtgsInfoExist.getStatus();
		/**
		 * If rtgs transaction present in db and its status is success or revered
		 * already
		 */
		if (rtgsInfoExist != null) {
			// NOTE:
			/**
			 * Fetching Previous Ft Response from database record
			 */
			if (statusExist == FtStatus.SUCCESS.getValue() || statusExist == FtStatus.REVERSED.getValue()) {

				String ftResponseStr = rtgsInfoExist.getFtResponseStr();
				ObjectMapper mapper = new ObjectMapper();
				FtTxResponse savedFtResponse = mapper.readValue(ftResponseStr, FtTxResponse.class);
				FtTxResponse ftResponse = savedFtResponse;

				rtgsInfoSave = rtgsInfoExist;
				rtgsInfoSave.setCoCode(rtgsInfoOutward.getCoCode());
				return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
				// }
			} else {

				logger.info("RTGS Outward already exists but failed or Pending");
				rtgsInfoSave = rtgsInfoExist;
				rtgsInfoSave.setCoCode(rtgsInfoOutward.getCoCode());
			}
		}

		// NOTE:
		/**
		 * When RTGS Not exists in database OR it exist but it's status is pending or
		 * failure then we have try again to reach CBS transaction and then Saving RTGS
		 * Data into the database again
		 */

		System.out.println(rtgsInfoSave.toString());

		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
		String host = httpServletRequest.getRemoteHost();

		if (remoteAddr == null) {
			remoteAddr = httpServletRequest.getRemoteAddr();
		}

		/*
		 * checking the categorty RTGS Ouward=1 Customs E payment=3, RTGS Inward=2
		 */

		int category = rtgsInfoOutward == null ? 0 : rtgsInfoOutward.getCategory();

		if (category == RTGSCategory.RTGSOUTWARDPACS8.getValue()
				&& rtgsInfoOutward.getDebitAmount() < RTGS_OUWARD_PACS8_MIN_VALUE) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FOURZ26.getText(),
					ResponseStatus.FOURZ26.getValue());
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {
			rtgsInfoSave.setCategory(rtgsInfoOutward.getCategory());
		}


		rtgsInfoSave.setIp(remoteAddr);
		rtgsInfoSave.setHostname(host);

		/**
		 * Initially the status of RTGS Transaction is pending and saved in the database
		 * It is because of Transaction response yet not confirmed
		 */

		rtgsInfoSave.setStatus(FtStatus.PENDING.getValue());
		rtgsInfoSave.setOfsRequest(requestOFS);

		// rtgsInfoSave =
		// RTGS Pending Status Saving
		rtgsInfoOutwardService.save(rtgsInfoSave);

		logger.info("RTGS saved as PENDING");

		String responseData = "";

		/**
		 * Checking if response from cbs is blank due to timeout or cbs issue
		 */

		try {
			responseData = tccUtility.sendRequest(requestOFS);
		} catch (BlankOfsResponseException e) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ4.getText(),
					ResponseStatus.FIVEZ4.getValue());
			
			ObjectMapper mapper = new ObjectMapper();
			String errorResponse = mapper.writeValueAsString(JwtErrorResponse.class);
			
			rtgsInfoSave.setStatus(FtStatus.FAILED.getValue());
			rtgsInfoSave.setFtResponseStr(errorResponse);
			rtgsInfoOutwardService.save(rtgsInfoSave);
			
			logger.error(e.getMessage());
			logger.error(jwtErrorResponse);
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		}

		/**
		 * Finally the RTGS Txn response is fetched
		 */

		System.out.println("responseData " + responseData);
		rtgsInfoSave.setIssueDate(new Timestamp(System.currentTimeMillis()));

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
            
			String errorResponse = Mapper.mapToJsonString(jwtErrorResponse);
			rtgsInfoSave.setStatus(FtStatus.FAILED.getValue());
			rtgsInfoSave.setFtResponseStr(errorResponse);
			
			rtgsInfoOutwardService.save(rtgsInfoSave);
			

			logger.error("Server Error:: " + jwtErrorResponse);
			// rtgsInfoOutwardService.save(rtgsInfoOutward);
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {

			// FtTxResponse ftResponse = new FtTxResponse();
			FtTxResponseBuilder ftResponse = FtTxResponse.builder();
			// NOTE: Not with Builder
			// ftResponse.setStatus(HttpStatus.OK);
			// ftResponse.setNarrative(creditNarrative);

			// NOTE: With Builder
			ftResponse.status(HttpStatus.OK).uniqueEft(rtgsInfoOutward.getUniqueOutwardRtgsId());

			/**
			 * NOTE: Wrapping the response by Response Handler
			 */

			ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
			wrapper = processor.handleResponseOfs(responseData,0);

			ftResponse.ftRef(wrapper.getFtRef()).message(wrapper.getMessage()).responseCode(wrapper.getResponseCode())
					.additionalInfo(wrapper.getAdditionalInfo()).timestamp(rtgsInfoSave.getIssueDate());

//             rtgsInfoOutward.setStatus(wrapper.getFtStatus());
			rtgsInfoSave.setStatus(wrapper.getFtStatus());

			ObjectMapper mapper = new ObjectMapper();
			String ftResponseStr = mapper.writeValueAsString(ftResponse.build());


			rtgsInfoSave.setFtResponseStr(ftResponseStr);
			rtgsInfoSave.setCbsFtno(ftResponse.build().getFtRef());
			rtgsInfoSave.setOfsResponse(responseData);

//             rtgsInfoSave = rtgsInfoOutward;
			// FIXME:
			try {
				rtgsInfoOutwardService.save(rtgsInfoSave);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info("-----::ERROR CHECKING----::" + rtgsInfoSave);
				logger.error(e.getMessage(), e);
				logger.info("JPA Error Occured During Save: " + e.getMessage());
			}

			return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

		}

	}

	/*
	 * ---------------------------- RTGS Outward Transaction Pacs 08 end ------------------------------------
	 * --------------------------------------------
	 */

	/*
	 * ---------------------------- RTGS Outward Transaction Pacs 09 start ----------------------------------
	 * --------------------------------------------
	 */

	public ResponseEntity<?> handleRtgsOutwardPacsNineTransaction(String requestOFS,
			RtgsInfoPacsNineOutward rtgsInfoPacsNineOut, HttpServletRequest httpServletRequest) throws Exception {

		System.out.println(requestOFS);

		TccUtility tccUtility = new TccUtility();

		RtgsInfoPacsNineOutward rtgsInfoExist = new RtgsInfoPacsNineOutward();
		RtgsInfoPacsNineOutward rtgsInfoSave = rtgsInfoPacsNineOut;
		rtgsInfoExist = rtgsInfoPacsNineOutwardService
				.findByUniqueOutwardRtgsId(rtgsInfoPacsNineOut.getUniqueOutwardRtgsId());
		int statusExist = rtgsInfoExist == null ? 0 : rtgsInfoExist.getStatus();
		/**
		 * If eft present in db and its status is success
		 */
		if (rtgsInfoExist != null) {

			logger.info("BEFTN Outward Alreeady Exists");
			// NOTE:
			/**
			 * Fetching Previous Ft Response from database record
			 */
			if (statusExist == FtStatus.SUCCESS.getValue() || statusExist == FtStatus.REVERSED.getValue()) {

				String ftResponseStr = rtgsInfoExist.getFtResponseStr();
				ObjectMapper mapper = new ObjectMapper();
				FtTxResponse savedFtResponse = mapper.readValue(ftResponseStr, FtTxResponse.class);
				FtTxResponse ftResponse = savedFtResponse;

				rtgsInfoSave = rtgsInfoExist;
				return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
				// }
			} else {
				rtgsInfoSave = rtgsInfoExist;
				rtgsInfoSave.setCoCode(rtgsInfoPacsNineOut.getCoCode());
			}
		}

		// NOTE:
		/**
		 * When RTGS Not exists in database OR it exist but it's status is pending or
		 * failure then we have try again to reach CBS transaction and then Saving RTGS
		 * PACS09 Data into the database again
		 */
//         System.out.println(rtgsInfoPacsNineOut.toString());
		System.out.println(rtgsInfoSave.toString());

		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
		String host = httpServletRequest.getRemoteHost();

		if (remoteAddr == null) {
			remoteAddr = httpServletRequest.getRemoteAddr();
		}

		/*
		 * checking the categorty RTGS Ouward=1 Customs E payment=3, RTGS Inward=2
		 */
		/*
		 * int category = rtgsInfoPacsNineOut == null ? 0 :
		 * rtgsInfoPacsNineOut.getCategory(); if (category ==
		 * RTGSCategory.RTGSOUTWARDPACS8.getValue() &&
		 * rtgsInfoPacsNineOut.getDebitAmount() < RTGS_OUWARD_PACS8_MIN_VALUE) {
		 * JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
		 * ResponseStatus.FOURZ26.getText(), ResponseStatus.FOURZ26.getValue()); return
		 * ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse); } else {
		 * rtgsInfoSave.setCategory(rtgsInfoPacsNineOut.getCategory()); }
		 */
		/*
		 * rtgsInfoPacsNineOut.setIp(remoteAddr); rtgsInfoPacsNineOut.setHostname(host);
		 */

		rtgsInfoSave.setIp(remoteAddr);
		rtgsInfoSave.setHostname(host);

		/**
		 * Initially the status of RTGS Transaction is pending and saved in the database
		 * It is because of Transaction response yet not confirmed
		 */

		/*
		 * rtgsInfoPacsNineOut.setStatus(EftStatus.PENDING.getValue());
		 * rtgsInfoPacsNineOut.setOfsRequest(requestOFS);
		 */

		rtgsInfoSave.setStatus(FtStatus.PENDING.getValue());
		rtgsInfoSave.setOfsRequest(requestOFS);

		// rtgsInfoSave =
			rtgsInfoPacsNineOutwardService.save(rtgsInfoSave);

		System.out.println("requestOFS: " + requestOFS);
		String responseData = "";

		/**
		 * Checking if response from cbs is blank due to timeout or cbs issue
		 */

		try {
			responseData = tccUtility.sendRequest(requestOFS);
		} catch (BlankOfsResponseException e) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ4.getText(),
					ResponseStatus.FIVEZ4.getValue());
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		}

		/**
		 * Finally the BEFTN Txn response is fetched
		 */

		// eftnInfoSave.setOfsResponse(responseData);
		// remitterInfoSave.setResponseAt(new Timestamp(System.currentTimeMillis()));
		System.out.println("responseData " + responseData);
		rtgsInfoSave.setIssueDate(new Timestamp(System.currentTimeMillis()));

		if (responseData.equals("400") || responseData.equals("401") || responseData.equals("402")
				|| responseData.equals("403")) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ3.getText(),
					ResponseStatus.FIVEZ3.getValue());
			rtgsInfoSave.setStatus(FtStatus.FAILED.getValue());
			rtgsInfoPacsNineOutwardService.save(rtgsInfoSave);
			// rtgsInfoOutwardService.save(rtgsInfoPacsNineOut);
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {

			// FtTxResponse ftResponse = new FtTxResponse();
			FtTxResponseBuilder ftResponse = FtTxResponse.builder();
			// NOTE: Not with Builder
			// ftResponse.setStatus(HttpStatus.OK);
			// ftResponse.setNarrative(creditNarrative);

			// NOTE: With Builder
			ftResponse.status(HttpStatus.OK).uniqueEft(rtgsInfoPacsNineOut.getUniqueOutwardRtgsId());

			/**
			 * NOTE: Wrapping the response by Response Handler
			 */

			ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
			wrapper = processor.handleResponseOfs(responseData,0);

			ftResponse.ftRef(wrapper.getFtRef()).message(wrapper.getMessage()).responseCode(wrapper.getResponseCode())
					.additionalInfo(wrapper.getAdditionalInfo()).timestamp(rtgsInfoSave.getIssueDate());

//             rtgsInfoPacsNineOut.setStatus(wrapper.getFtStatus());
			rtgsInfoSave.setStatus(wrapper.getFtStatus());

			ObjectMapper mapper = new ObjectMapper();
			String ftResponseStr = mapper.writeValueAsString(ftResponse.build());
			/*
			 * rtgsInfoPacsNineOut.setFtResponseStr(ftResponseStr);
			 * rtgsInfoPacsNineOut.setCbsFtno(ftResponse.build().getFtRef());
			 * rtgsInfoPacsNineOut.setOfsResponse(responseData);
			 */

			rtgsInfoSave.setFtResponseStr(ftResponseStr);
			rtgsInfoSave.setCbsFtno(ftResponse.build().getFtRef());
			rtgsInfoSave.setOfsResponse(responseData);

//             rtgsInfoSave = rtgsInfoPacsNineOut;
			// FIXME:
			try {
				rtgsInfoPacsNineOutwardService.save(rtgsInfoSave);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info("-----::ERROR CHECKING----::" + rtgsInfoSave);
				logger.error(e.getMessage(), e);
				logger.info("JPA Error Occured During Save: " + e.getMessage());
			}
			// eftInfoOutwardService.save(eftInfoOutward);

			return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

		}

	}

	/*
	 * ---------------------------- RTGS Outward Transaction Pacs 09 end
	 */

	/*------------------------------------ Inward Transaction ----------------------------------------------- */
	
	/*
	 * ---------------------------- RTGS Inward Transaction Pacs 08 --------------------------------------*/

	public ResponseEntity<?> handleRtgsInwardTransaction(String requestOFS, RtgsInfoInward rtgsInfoInward,
			HttpServletRequest httpServletRequest) throws Exception {

		System.out.println(requestOFS);

		TccUtility tccUtility = new TccUtility();

		RtgsInfoInward rtgsInfoExist = new RtgsInfoInward();
//         RtgsInfoOutward rtgsInfoSave = new RtgsInfoOutward();
		RtgsInfoInward rtgsInfoSave = rtgsInfoInward;
		rtgsInfoExist = rtgsInfoInwardService.findByUniqueInwardRtgsId(rtgsInfoInward.getUniqueInwardRtgsId());
		int statusExist = rtgsInfoExist == null ? 0 : rtgsInfoExist.getStatus();
		/**
		 * If eft present in db and its status is success
		 */
		if (rtgsInfoExist != null) {
			// NOTE:
			/**
			 * Fetching Previous Ft Response from database record
			 */
			if (statusExist == FtStatus.SUCCESS.getValue()) {

				String ftResponseStr = rtgsInfoExist.getFtResponseStr();
				ObjectMapper mapper = new ObjectMapper();
				FtTxResponse savedFtResponse = mapper.readValue(ftResponseStr, FtTxResponse.class);
				FtTxResponse ftResponse = savedFtResponse;

				rtgsInfoSave = rtgsInfoExist;
				return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
				// }
			} else {
				rtgsInfoSave = rtgsInfoExist;
				rtgsInfoSave.setCoCode(rtgsInfoInward.getCoCode());
			}
		}

		// NOTE:
		/**
		 * When RTGS Not exists in database OR it exist but it's status is pending or
		 * failure then we have try again to reach CBS transaction and then Saving BEFTN
		 * Data into the database again
		 */
//         System.out.println(rtgsInfoOutward.toString());
		System.out.println(rtgsInfoSave.toString());

		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
		String host = httpServletRequest.getRemoteHost();

		if (remoteAddr == null) {
			remoteAddr = httpServletRequest.getRemoteAddr();
		}

		/*
		 * checking the categorty RTGS Ouward=1 Customs E payment=3, RTGS Inward=2
		 */

		/*
		 * int category = rtgsInfoInward == null ? 0 : rtgsInfoInward.getCategory(); if
		 * (category == RTGSCategory.RTGSOUTWARDPACS8.getValue() &&
		 * rtgsInfoOutward.getDebitAmount() < RTGS_OUWARD_PACS8_MIN_VALUE) {
		 * JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
		 * ResponseStatus.FOURZ26.getText(), ResponseStatus.FOURZ26.getValue()); return
		 * ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse); } else {
		 * rtgsInfoSave.setCategory(rtgsInfoOutward.getCategory()); }
		 */

		/*
		 * rtgsInfoOutward.setIp(remoteAddr); rtgsInfoOutward.setHostname(host);
		 */

		rtgsInfoSave.setIp(remoteAddr);
		rtgsInfoSave.setHostname(host);

		/**
		 * Initially the status of BEFTN Transaction is pending and saved in the
		 * database It is because of Transaction response yet not confirmed
		 */

		/*
		 * rtgsInfoOutward.setStatus(EftStatus.PENDING.getValue());
		 * rtgsInfoOutward.setOfsRequest(requestOFS);
		 */

		rtgsInfoSave.setStatus(FtStatus.PENDING.getValue());
		rtgsInfoSave.setOfsRequest(requestOFS);

		// rtgsInfoSave =
		rtgsInfoInwardService.save(rtgsInfoSave);

		System.out.println("requestOFS: " + requestOFS);
		String responseData = "";

		/**
		 * Checking if response from cbs is blank due to timeout or cbs issue
		 */

		try {
			responseData = tccUtility.sendRequest(requestOFS);
		} catch (BlankOfsResponseException e) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ4.getText(),
					ResponseStatus.FIVEZ4.getValue());
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		}

		/**
		 * Finally the BEFTN Txn response is fetched
		 */

		// eftnInfoSave.setOfsResponse(responseData);
		// remitterInfoSave.setResponseAt(new Timestamp(System.currentTimeMillis()));
		System.out.println("responseData " + responseData);
		rtgsInfoSave.setIssueDate(new Timestamp(System.currentTimeMillis()));

		if (responseData.equals("400") || responseData.equals("401") || responseData.equals("402")
				|| responseData.equals("403")) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ3.getText(),
					ResponseStatus.FIVEZ3.getValue());
			rtgsInfoSave.setStatus(FtStatus.FAILED.getValue());
			rtgsInfoInwardService.save(rtgsInfoSave);
			// rtgsInfoOutwardService.save(rtgsInfoOutward);
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {

			// FtTxResponse ftResponse = new FtTxResponse();
			FtTxResponseBuilder ftResponse = FtTxResponse.builder();
			// NOTE: Not with Builder
			// ftResponse.setStatus(HttpStatus.OK);
			// ftResponse.setNarrative(creditNarrative);

			// NOTE: With Builder
			ftResponse.status(HttpStatus.OK).uniqueEft(rtgsInfoInward.getUniqueInwardRtgsId());

			/**
			 * NOTE: Wrapping the response by Response Handler
			 */

			ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
			wrapper = processor.handleResponseOfs(responseData,0);

			ftResponse.ftRef(wrapper.getFtRef()).message(wrapper.getMessage()).responseCode(wrapper.getResponseCode())
					.additionalInfo(wrapper.getAdditionalInfo()).timestamp(rtgsInfoSave.getIssueDate());
//             rtgsInfoOutward.setStatus(wrapper.getFtStatus());
			rtgsInfoSave.setStatus(wrapper.getFtStatus());

			ObjectMapper mapper = new ObjectMapper();
			String ftResponseStr = mapper.writeValueAsString(ftResponse.build());
			/*
			 * rtgsInfoOutward.setFtResponseStr(ftResponseStr);
			 * rtgsInfoOutward.setCbsFtno(ftResponse.build().getFtRef());
			 * rtgsInfoOutward.setOfsResponse(responseData);
			 */

			rtgsInfoSave.setFtResponseStr(ftResponseStr);
			rtgsInfoSave.setCbsFtno(ftResponse.build().getFtRef());
			rtgsInfoSave.setOfsResponse(responseData);

//             rtgsInfoSave = rtgsInfoOutward;
			// FIXME:
			try {
				rtgsInfoInwardService.save(rtgsInfoSave);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info("-----::ERROR CHECKING----::" + rtgsInfoSave);
				logger.error(e.getMessage(), e);
				logger.info("JPA Error Occured During Save: " + e.getMessage());
			}
			// eftInfoOutwardService.save(eftInfoOutward);

			return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

		}

	}

	/*------------------------- Inward Transaction PACS 08 end -------------------------------------------------*/

	/*
	 * ---------------------------- RTGS Inward Transaction PACS 09 start
	 * --------------------------------------------
	 */
	/*
	 * ---------------------------- RTGS PACS 09 only for Foreign Currency
	 * -------------------------------------------
	 */

	public ResponseEntity<?> handleRtgsInwardPacsNineTransaction(String requestOFS,
			RtgsInfoPacsNineInward rtgsInfoPacsNineIn, HttpServletRequest httpServletRequest) throws Exception {

		System.out.println(requestOFS);

		TccUtility tccUtility = new TccUtility();

		RtgsInfoPacsNineInward rtgsInfoExist = new RtgsInfoPacsNineInward();
		RtgsInfoPacsNineInward rtgsInfoSave = rtgsInfoPacsNineIn;
		rtgsInfoExist = rtgsInfoPacsNineInwardService
				.findByUniqueInwardRtgsId(rtgsInfoPacsNineIn.getUniqueInwardRtgsId());
		int statusExist = rtgsInfoExist == null ? 0 : rtgsInfoExist.getStatus();
		/**
		 * If eft present in db and its status is success
		 */
		if (rtgsInfoExist != null) {
			// NOTE:
			/**
			 * Fetching Previous Ft Response from database record
			 */
			if (statusExist == FtStatus.SUCCESS.getValue()) {

				String ftResponseStr = rtgsInfoExist.getFtResponseStr();
				ObjectMapper mapper = new ObjectMapper();
				FtTxResponse savedFtResponse = mapper.readValue(ftResponseStr, FtTxResponse.class);
				FtTxResponse ftResponse = savedFtResponse;

				rtgsInfoSave = rtgsInfoExist;
				return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
				// }
			} else {
				rtgsInfoSave = rtgsInfoExist;
				rtgsInfoSave.setCoCode(rtgsInfoPacsNineIn.getCoCode());
			}
		}

		// NOTE:
		/**
		 * When BEFTN Not exists in database OR it exist but it's status is pending or
		 * failure then we have try again to reach CBS transaction and then Saving BEFTN
		 * Data into the database again
		 */
//         System.out.println(rtgsInfoPacsNineOut.toString());
		System.out.println(rtgsInfoSave.toString());

		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
		String host = httpServletRequest.getRemoteHost();

		if (remoteAddr == null) {
			remoteAddr = httpServletRequest.getRemoteAddr();
		}

		/*
		 * checking the categorty PACS09 RTGS Ouward=1 PACS 09 RTGS Inward=2
		 */
		/*
		 * int category = rtgsInfoPacsNineOut == null ? 0 :
		 * rtgsInfoPacsNineOut.getCategory(); if (category ==
		 * RTGSCategory.RTGSOUTWARDPACS8.getValue() &&
		 * rtgsInfoPacsNineOut.getDebitAmount() < RTGS_OUWARD_PACS8_MIN_VALUE) {
		 * JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
		 * ResponseStatus.FOURZ26.getText(), ResponseStatus.FOURZ26.getValue()); return
		 * ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse); } else {
		 * rtgsInfoSave.setCategory(rtgsInfoPacsNineOut.getCategory()); }
		 */
		/*
		 * rtgsInfoPacsNineOut.setIp(remoteAddr); rtgsInfoPacsNineOut.setHostname(host);
		 */

		rtgsInfoSave.setIp(remoteAddr);
		rtgsInfoSave.setHostname(host);

		/**
		 * Initially the status of BEFTN Transaction is pending and saved in the
		 * database It is because of Transaction response yet not confirmed
		 */

		/*
		 * rtgsInfoPacsNineOut.setStatus(EftStatus.PENDING.getValue());
		 * rtgsInfoPacsNineOut.setOfsRequest(requestOFS);
		 */

		rtgsInfoSave.setStatus(FtStatus.PENDING.getValue());
		rtgsInfoSave.setOfsRequest(requestOFS);

		// rtgsInfoSave =
		rtgsInfoPacsNineInwardService.save(rtgsInfoSave);

		System.out.println("requestOFS: " + requestOFS);
		String responseData = "";

		/**
		 * Checking if response from cbs is blank due to timeout or cbs issue
		 */

		try {
			responseData = tccUtility.sendRequest(requestOFS);
		} catch (BlankOfsResponseException e) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ4.getText(),
					ResponseStatus.FIVEZ4.getValue());
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		}

		/**
		 * Finally the BEFTN Txn response is fetched
		 */

		// eftnInfoSave.setOfsResponse(responseData);
		// remitterInfoSave.setResponseAt(new Timestamp(System.currentTimeMillis()));
		System.out.println("responseData " + responseData);
		rtgsInfoSave.setIssueDate(new Timestamp(System.currentTimeMillis()));

		if (responseData.equals("400") || responseData.equals("401") || responseData.equals("402")
				|| responseData.equals("403")) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ3.getText(),
					ResponseStatus.FIVEZ3.getValue());
			rtgsInfoSave.setStatus(FtStatus.FAILED.getValue());
			rtgsInfoPacsNineInwardService.save(rtgsInfoSave);
			// rtgsInfoOutwardService.save(rtgsInfoPacsNineOut);
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {

			// FtTxResponse ftResponse = new FtTxResponse();
			FtTxResponseBuilder ftResponse = FtTxResponse.builder();
			// NOTE: Not with Builder
			// ftResponse.setStatus(HttpStatus.OK);
			// ftResponse.setNarrative(creditNarrative);

			// NOTE: With Builder
			ftResponse.status(HttpStatus.OK).uniqueEft(rtgsInfoPacsNineIn.getUniqueInwardRtgsId());

			/**
			 * NOTE: Wrapping the response by Response Handler
			 */

			ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
			wrapper = processor.handleResponseOfs(responseData,0);

			ftResponse.ftRef(wrapper.getFtRef()).message(wrapper.getMessage()).responseCode(wrapper.getResponseCode())
					.additionalInfo(wrapper.getAdditionalInfo()).timestamp(rtgsInfoSave.getIssueDate());
//             rtgsInfoPacsNineOut.setStatus(wrapper.getFtStatus());
			rtgsInfoSave.setStatus(wrapper.getFtStatus());

			ObjectMapper mapper = new ObjectMapper();
			String ftResponseStr = mapper.writeValueAsString(ftResponse.build());
			/*
			 * rtgsInfoPacsNineOut.setFtResponseStr(ftResponseStr);
			 * rtgsInfoPacsNineOut.setCbsFtno(ftResponse.build().getFtRef());
			 * rtgsInfoPacsNineOut.setOfsResponse(responseData);
			 */

			rtgsInfoSave.setFtResponseStr(ftResponseStr);
			rtgsInfoSave.setCbsFtno(ftResponse.build().getFtRef());
			rtgsInfoSave.setOfsResponse(responseData);

//             rtgsInfoSave = rtgsInfoPacsNineOut;
			// FIXME:

			try {
				rtgsInfoPacsNineInwardService.save(rtgsInfoSave);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info("-----::ERROR CHECKING----::" + rtgsInfoSave);
				logger.error(e.getMessage(), e);
				logger.info("JPA Error Occured During Save: " + e.getMessage());
			}

			return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

		}

	}

	/*
	 * ---------------------------- RTGS Inward Transaction PACS 09 end
	 * --------------------------------------------
	 */

}