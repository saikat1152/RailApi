package com.jbl.t24.rest.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementInInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementNineInInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementNineOutInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementOutInfo;
import com.jbl.t24.rest.api.service.rtgs.FtHandlerService;
import com.jbl.t24.rest.api.service.rtgs.FtHandlerServiceN;
import com.jbl.t24.rest.api.service.rtgs.OfsMessageGenerator;
import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;

@RestController
@CrossOrigin
@RequestMapping("/rtgs")
@Validated
public class RTGSSettlementController {

	@Autowired
	FtHandlerServiceN ftHandlerServiceN;

	Logger logger = LogManager.getLogger(RTGSSettlementController.class);


	@PostMapping(value = "/settlement-in", consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> settlementInward(@Valid @RequestBody RTGSSettlementInInfo settlementInfo,
			HttpServletRequest httpServletRequest) throws Exception {

		final String requestOFS = OfsMessageGenerator.generateOfsMessage(settlementInfo,
				settlementInfo.getUniqueSettlementtId(), httpServletRequest);
		ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, settlementInfo,
				settlementInfo.getUniqueSettlementtId());

		System.out.println(response.getBody());
		logger.info("RTGS Inward Settlement Process Finished");
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		System.out.println("---- " + settlementInfo);
		return response;

	}

	@PostMapping(value = "/settlement-out", consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> settlementOutward(@Valid @RequestBody RTGSSettlementOutInfo settlementOutInfo,
			HttpServletRequest httpServletRequest) throws Exception {

		final String requestOFS = OfsMessageGenerator.generateOfsMessage(settlementOutInfo,
				settlementOutInfo.getUniqueSettlementtId(),httpServletRequest);
		ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, settlementOutInfo,
				settlementOutInfo.getUniqueSettlementtId());

		System.out.println(response.getBody());
		logger.info("RTGS Outward Settlement Process Finished");
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		System.out.println("---- " + settlementOutInfo);
		return response;
	}


	@PostMapping(value = "/settlement-nine-in", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> settlementNineInward(@Valid @RequestBody RTGSSettlementNineInInfo settlementNineInfo,
			HttpServletRequest httpServletRequest) throws Exception {
	
		final String requestOFS = OfsMessageGenerator.generateOfsMessage(settlementNineInfo,
		settlementNineInfo.getUniqueSettlementtId(), httpServletRequest);
		ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, settlementNineInfo,
		settlementNineInfo.getUniqueSettlementtId());

		System.out.println(response.getBody());
		logger.info("RTGS Pacs09 Inward Settlement Process Finished");
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		System.out.println("---- " + settlementNineInfo);
		return response;

	}


	@PostMapping(value = "/settlement-nine-out", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> settlementNineOutward(@Valid @RequestBody RTGSSettlementNineOutInfo settlementNineOutInfo,
			HttpServletRequest httpServletRequest) throws Exception {

		final String requestOFS = OfsMessageGenerator.generateOfsMessage(settlementNineOutInfo,
		settlementNineOutInfo.getUniqueSettlementtId(),httpServletRequest);
		ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, settlementNineOutInfo,
		settlementNineOutInfo.getUniqueSettlementtId());

		System.out.println(response.getBody());
		logger.info("RTGS Pacs09 Outward Settlement Process Finished");
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		System.out.println("---- " + settlementNineOutInfo);
		return response;
	}

}
