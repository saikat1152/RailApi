package com.jbl.t24.rest.api.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jbl.t24.rest.api.common.model.AccountInfo;
import com.jbl.t24.rest.api.common.model.AccountInfoNotFound;
import com.jbl.t24.rest.api.common.model.FtResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.custom.exception.BlankOfsResponseException;
import com.jbl.t24.rest.api.custom.validation.CustomValidation;
import com.jbl.t24.rest.api.enums.AccountNature;
import com.jbl.t24.rest.api.enums.CBSResponseStr;
import com.jbl.t24.rest.api.enums.RemitterStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.BeftnOutwardInfo;
import com.jbl.t24.rest.api.model.EftInfoInward;
import com.jbl.t24.rest.api.model.EftInfoOutward;
import com.jbl.t24.rest.api.model.RequestOfs;
import com.jbl.t24.rest.api.service.BeftnInfoService;
import com.jbl.t24.rest.api.service.FtHandlerService;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@RestController
@CrossOrigin
@RequestMapping("/beftn_out_dummy")
@Validated
public class BeftnOutwardDummy {

	@Autowired
	private BeftnInfoService beftnInfoService;

	@Autowired
	private FtHandlerService ftHandlerService;

	@RequestMapping(value = "/out", method = RequestMethod.GET)

	public ResponseEntity<?> beftnTrasferOutward(@Valid @RequestParam Map<String, String> requestParams,
			HttpServletRequest httpServletRequest) throws Exception {

		/**
		 * Setting the versionwise OFS Message @requestOFS
		 */
		/**
		 * ACOD --> BEFTN Outward
		 * ACOR ---> RTGS Outward
		 * ACIR ---> RTGS Inward
		 * 
		 */

		/**
		 * BEFT , RTGS, BACH
		 * Current Timestamp YYYYMMDD HH:MM:SS
		 * Unique ID
		 */

		/**
		 * Currency
		 * USD, GBP, EUR,
		 */
		String requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0,BD0010888,,TRANSACTION.TYPE=ACOD,DEBIT.ACCT.NO=0100146594209,DEBIT.CURRENCY=BDT,DEBIT.AMOUNT=10,DEBIT.VALUE.DATE=20220506,CREDIT.ACCT.NO=BDT171120001,ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1,FT.DR.DETAILS=BACH,FT.DR.DETAILS=,COMMISSION.TYPE=,CHEQUE.NUMBER=,LOCAL.REF:3:1=,LOCAL.REF:94:1=0021699806164052";

		// BeftnOutwardInfo beftnInfoSave = null;
		// BeftnOutwardInfo beftnInfoExist = null;

		TccUtility tccUtility = new TccUtility();

		String ofsResponse = tccUtility.sendRequest(requestOFS);
		String[] spiltDataOfs = ofsResponse.split(",");
		// String[] firstPart = spiltDataOfs[0].split("/");
		// String statusFlag = firstPart[2];

		return ResponseEntity.status(HttpStatus.OK).body(ofsResponse);

	}

	@RequestMapping(value = "/outward", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> beftnTrasferInward(@Valid @RequestBody EftInfoOutward eftnInfoOut,
			HttpServletRequest httpServletRequest) throws Exception {

		String uniqueFtId = eftnInfoOut.getUniqueOutwardEftId();
		String coCode = eftnInfoOut.getCoCode();
		String companyCode = eftnInfoOut.getCompanyCode() + coCode;
		eftnInfoOut.setCompanyCode(companyCode);
		String txType = eftnInfoOut.getTxType();
		String debitAccNo = eftnInfoOut.getDebitAccNo();
		String currency = eftnInfoOut.getCurrency();
		String debitAmount = String.format("%.2f", eftnInfoOut.getDebitAmount());
		String creditAccNo = eftnInfoOut.getCreditAccNo();
		String debitDetails = eftnInfoOut.getDebitDetails();
		String creditDetails = eftnInfoOut.getCreditDetails();
		String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		Timestamp t = new Timestamp(new Date().getTime());
		eftnInfoOut.setIssueDate(t);

		Objects.requireNonNull(uniqueFtId);
		Objects.requireNonNull(coCode);
		Objects.requireNonNull(debitAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(currency);

		// String requestOFS = "";

		issueDate = "20220506";
		final String requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0,"
				+ companyCode
				+ ",,TRANSACTION.TYPE=" + txType + ","
				+ "DEBIT.ACCT.NO=" + debitAccNo + ","
				+ "DEBIT.CURRENCY=" + currency + ","
				+ "DEBIT.AMOUNT=" + debitAmount + ","
				+ "DEBIT.VALUE.DATE=" + issueDate + ","
				+ "CREDIT.ACCT.NO=" + creditAccNo + ","
				+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1,"
				+ "FT.DR.DETAILS=" + debitDetails + ","
				+ "FT.CR.DETAILS=" + creditDetails + ","
				+ "COMMISSION.TYPE=,"
				+ "CHEQUE.NUMBER=,"
				+ "LOCAL.REF:3:1=,"
				+ "LOCAL.REF:94:1=" + uniqueFtId;

		ResponseEntity<?> response = ftHandlerService.handleFtTransaction(requestOFS, eftnInfoOut, httpServletRequest);
		
		System.out.println(response.getBody());
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		return response;

	}


}
