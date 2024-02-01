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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementInInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementOutInfo;
import com.jbl.t24.rest.api.service.rtgs.FtHandlerService;
import com.jbl.t24.rest.api.service.rtgs.FtHandlerServiceN;
import com.jbl.t24.rest.api.service.rtgs.OfsMessageGenerator;

@RestController
@CrossOrigin
@RequestMapping("/rtgs")
@Validated
public class RTGSSettlementController {

    @Autowired
    FtHandlerServiceN ftHandlerServiceN;

    Logger logger = LogManager.getLogger(FtHandlerService.class);

    @RequestMapping(value = "/settlement-in", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<?> settlementInward(@Valid @RequestBody RTGSSettlementInInfo settlementInfo,
            HttpServletRequest httpServletRequest) throws Exception {
    	

    	if(!settlementInfo.getTxCategory().equals("PACS08-Inward") && !settlementInfo.getTxCategory().equals("PACS09-Inward")) {
        	JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FOURZ28.getText(),
                    ResponseStatus.FOURZ28.getValue());
        	return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
        }

        final String requestOFS = OfsMessageGenerator.generateOfsMessage(settlementInfo, settlementInfo.getUniqueSettlementtId());
        ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, settlementInfo, settlementInfo.getUniqueSettlementtId());

        System.out.println(response.getBody());
        logger.info("RTGS Inward Settlement Process Finished");
        // return ResponseEntity.status(HttpStatus.OK).body(response);
        System.out.println("---- "+settlementInfo);
        return response;

    }

    @RequestMapping(value = "/settlement-out", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<?> settlementOutward(@Valid @RequestBody RTGSSettlementOutInfo settlementOutInfo,
            HttpServletRequest httpServletRequest) throws Exception {
    	
    	if(!settlementOutInfo.getTxCategory().equals("PACS08-Outward") && !settlementOutInfo.getTxCategory().equals("PACS09-Outward")) {
        	JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FOURZ28.getText(),
                    ResponseStatus.FOURZ28.getValue());
        	return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
        }


        final String requestOFS = OfsMessageGenerator.generateOfsMessage(settlementOutInfo, settlementOutInfo.getUniqueSettlementtId());
        ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, settlementOutInfo, settlementOutInfo.getUniqueSettlementtId());

        System.out.println(response.getBody());
        logger.info("RTGS Outward Settlement Process Finished");
        // return ResponseEntity.status(HttpStatus.OK).body(response);
        System.out.println("---- "+settlementOutInfo);
        return response;
    }

}
