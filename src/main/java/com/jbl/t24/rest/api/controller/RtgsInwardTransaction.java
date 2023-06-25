package com.jbl.t24.rest.api.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.jbl.t24.rest.api.config.HostIpHandle;
import com.jbl.t24.rest.api.model.RtgsInfoInward;
import com.jbl.t24.rest.api.service.FtHandlerService;
import com.jbl.t24.rest.api.service.FtHandlerServiceN;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@RestController
@CrossOrigin
@RequestMapping("/rtgs_in")
@Validated
public class RtgsInwardTransaction {

	@Autowired
	private FtHandlerService ftHandlerService;
	
	@Autowired
	private FtHandlerServiceN ftHandlerServiceN;

	public ResponseEntity<?> rtgsTrasferInward(@Valid @RequestParam Map<String, String> requestParams,
			HttpServletRequest httpServletRequest) throws Exception {

		/**
		 * Setting the versionwise OFS Message @requestOFS
		 */
		/**
		 * ACOD --> BEFTN Outward
		 * ACOR ---> RTGS Outward
		 * ACIR ---> RTGS Inward
		 * ACOP ---> RTGS Outward PACS 09 FC
		 * ACIN ---> RTGS Inward PACS 09 FC
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
		String requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0,BD0010888,,TRANSACTION.TYPE=ACOR,DEBIT.ACCT.NO=0100146594209,DEBIT.CURRENCY=BDT,DEBIT.AMOUNT=10,DEBIT.VALUE.DATE=20220506,CREDIT.ACCT.NO=BDT171120001,ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1,FT.DR.DETAILS=BACH,FT.DR.DETAILS=,COMMISSION.CODE=,COMMISSION.TYPE=,CHEQUE.NUMBER=,LOCAL.REF:3:1=,LOCAL.REF:94:1=0021699806164052";

		// BeftnOutwardInfo beftnInfoSave = null;
		// BeftnOutwardInfo beftnInfoExist = null;

		TccUtility tccUtility = new TccUtility();

		String ofsResponse = tccUtility.sendRequest(requestOFS);
		String[] spiltDataOfs = ofsResponse.split(",");
		// String[] firstPart = spiltDataOfs[0].split("/");
		// String statusFlag = firstPart[2];

		return ResponseEntity.status(HttpStatus.OK).body(ofsResponse);

	}

	
	@RequestMapping(value = "/inward", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> rtgsTrasferInward(@Valid @RequestBody RtgsInfoInward rtgsInfoInward,
			HttpServletRequest httpServletRequest) throws Exception {

		String uniqueFtId = rtgsInfoInward.getUniqueInwardRtgsId();
		String coCode = rtgsInfoInward.getCoCode();
		String companyCode = rtgsInfoInward.getCompanyCode() + coCode;
		rtgsInfoInward.setCompanyCode(companyCode);
		String txType = rtgsInfoInward.getTxType();
		String debitAccNo = rtgsInfoInward.getDebitAccNo();
		String currency = rtgsInfoInward.getCurrency();
		String debitAmount = String.format("%.2f", rtgsInfoInward.getDebitAmount());
		String creditAccNo = rtgsInfoInward.getCreditAccNo();
		String debitDetails = rtgsInfoInward.getDebitDetails();
		String creditDetails = rtgsInfoInward.getCreditDetails();
		String commissionCode = rtgsInfoInward.getCommissionCode();
		String commissionType = rtgsInfoInward.getCommissionType();
		String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		Timestamp t = new Timestamp(new Date().getTime());
		rtgsInfoInward.setIssueDate(t);

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
				+ "COMMISSION.CODE=" + commissionCode+ ","
				+ "COMMISSION.TYPE="+ commissionType +","
				+ "CHEQUE.NUMBER=,"
				+ "LOCAL.REF:3:1=,"
				+ "LOCAL.REF:94:1=" + uniqueFtId;
		

		Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		rtgsInfoInward.setHostname(hostIpData.get("host"));
		rtgsInfoInward.setIp(hostIpData.get("remoteAddr"));

//		ResponseEntity<?> response = ftHandlerService.handleRtgsInwardTransaction(requestOFS, rtgsInfoInward, httpServletRequest);
		ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, rtgsInfoInward, uniqueFtId);
		
		System.out.println(response.getBody());
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		return response;

	}
	

}
