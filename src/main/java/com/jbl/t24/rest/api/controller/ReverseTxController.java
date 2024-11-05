package com.jbl.t24.rest.api.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.FtTxResponse.FtTxResponseBuilder;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.config.Mapper;
import com.jbl.t24.rest.api.constant.OfsSources;
import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.rtgs.CommonFtInfo;
import com.jbl.t24.rest.api.model.rtgs.RtgsCommon;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoOutward;
import com.jbl.t24.rest.api.service.rtgs.ResponseMessageProcessor;
import com.jbl.t24.rest.api.service.rtgs.RtgsInfoOutwardService;
import com.jbl.t24.rest.api.service.rtgs.RtgsInfoPacsNineOutwardService;
import com.jbl.t24.rest.api.service.rtgs.RtgsInfoService;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@RestController
@CrossOrigin
@RequestMapping("/rtgs")
@Validated
public class ReverseTxController {
	Logger logger = LogManager.getLogger(ReverseTxController.class);

	@Autowired
	RtgsInfoOutwardService rtgsInfoOutwardService;

	@Autowired
	RtgsInfoPacsNineOutwardService infoPacsNineOutwardService;

	@Autowired
	RtgsInfoService infoService;

	@Autowired
	ResponseMessageProcessor processor = new ResponseMessageProcessor();

	@PostMapping("/reverse")
	public ResponseEntity<?> reverseTransactionHandle(@Valid @RequestBody Map<String, String> requestParams,
			HttpServletRequest httpServletRequest) throws Exception {

		// Capture the current timestamp
		Instant instant = Instant.now();

		// Convert the Instant to a java.sql.Timestamp
		Timestamp currentHIttingTimeStamp = Timestamp.from(instant);

		logger.info("Reverse Tx Performed");
		String cbsFtNo = requestParams.get("ftNumber");
		// CommonFtInfo rtgsOutward =
		// infoService.findByCbsFtNo(requestParams.get("rtgs-version"), cbsFtNo);

		RtgsCommon rtgsOutward = infoService.findByCbsFtNo(requestParams.get("rtgs-version"), cbsFtNo);

		// RtgsInfoOutward rtgsInfoOutward =
		// rtgsInfoOutwardService.findByCbsFtno(cbsFtNo);
		if (rtgsOutward == null) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FOURZ29.getText(),
					ResponseStatus.FOURZ29.getValue());
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		}
		if (rtgsOutward.getStatus() == FtStatus.REVERSED.getValue()) {

			String ftResponseStr = rtgsOutward.getFtResponseStr();
			// ObjectMapper mapper = new ObjectMapper();
			// FtTxResponse savedFtResponse = mapper.readValue(ftResponseStr,
			// FtTxResponse.class);
			FtTxResponse savedFtResponse = Mapper.readValue(ftResponseStr);
			FtTxResponse ftResponse = savedFtResponse;
			ftResponse.setMessage(ResponseStatus.TWOZ6.getText());
			ftResponse.setResponseCode(ResponseStatus.TWOZ6.getValue());
			ftResponse.setTimestamp(rtgsOutward.getIssueDate());
			ftResponse.setReverseTimestamp(rtgsOutward.getReverseDate());
			return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
		}
		// String requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/R/PROCESS//0,BD001" +
		// rtgsOutward.getCoCode() + "," + cbsFtNo;

		String requestOFS = OfsSources.REVERSE_OFS_STRING + rtgsOutward.getCoCode() + "," + cbsFtNo;

		TccUtility tccUtility = new TccUtility();

		String responseData = tccUtility.sendRequest(requestOFS);
		// String[] spiltDataOfs = ofsResponse.split(",");

		FtTxResponseBuilder ftResponse = FtTxResponse.builder();

		ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
		// wrapper = processor.handleResponseOfs(responseData, 1);
		wrapper = processor.handleResponseOfs(responseData, 1);

		FtTxResponse ftTxResponse = Mapper.readValue(rtgsOutward.getFtResponseStr());

		ftResponse.ftRef(wrapper.getFtRef()).message(wrapper.getMessage()).responseCode(wrapper.getResponseCode())
				.additionalInfo(wrapper.getAdditionalInfo()).uniqueRTGS(wrapper.getUniqueOperationTransactionId())
				.status(HttpStatus.OK).uniqueRTGS(ftTxResponse.getUniqueRTGS());

		rtgsOutward.setStatus(wrapper.getFtStatus());
		// rtgsOutward.setReverseDate(new Timestamp(System.currentTimeMillis()));
		rtgsOutward.setReverseDate(currentHIttingTimeStamp);

		ftResponse.timestamp(currentHIttingTimeStamp);
		ftResponse.reverseTimestamp(currentHIttingTimeStamp);

		String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());
		;
		rtgsOutward.setFtResponseStr(ftResponseStr);

		infoService.save(rtgsOutward);

		logger.info("Reverse Transaction Finished");

		return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

	}

}
