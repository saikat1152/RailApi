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
import com.jbl.t24.rest.api.model.RtgsInfoOutward;
import com.jbl.t24.rest.api.model.RtgsInfoPacsNineOutward;
import com.jbl.t24.rest.api.service.BeftnInfoService;
import com.jbl.t24.rest.api.service.FtHandlerService;
import com.jbl.t24.rest.api.service.FtHandlerServiceN;
import com.jbl.t24.rest.api.service.RtgsHandlerService;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@RestController
@CrossOrigin
@RequestMapping("/rtgs_out")
@Validated
public class RtgsOutwardPacsNineTransaction {

	@Autowired
	private FtHandlerService ftHandlerService;

	@Autowired
	private FtHandlerServiceN ftHandlerServiceN;

	@RequestMapping(value = "/pacs09/out", method = RequestMethod.GET)

	public ResponseEntity<?> beftnTrasferOutward(@Valid @RequestParam Map<String, String> requestParams,
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
		String requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0,,TRANSACTION.TYPE=ACOP,DEBIT.ACCT.NO=USD1720600010888,DEBIT.CURRENCY=USD,DEBIT.AMOUNT=10,DEBIT.VALUE.DATE=20220506,CREDIT.ACCT.NO=USD1745100059999,ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1,FT.DR.DETAILS=RTGS-PACS9,FT.CR.DETAILS=,COMMISSION.TYPE=,CHEQUE.NUMBER=,LOCAL.REF:3:1=,LOCAL.REF:94:1=RTGSPACS90001,LOCAL.REF:125:1=OTHER INFO,LOCAL.REF:126:1=TEST BILL,LOCAL.REF:127:1=TEST LC,LOCAL.REF:128:1=TEST NAME,LOCAL.REF:129:1=TEST INSTR,LOCAL.REF:130:1=TF00110000";

		// BeftnOutwardInfo beftnInfoSave = null;
		// BeftnOutwardInfo beftnInfoExist = null;

		TccUtility tccUtility = new TccUtility();

		String ofsResponse = tccUtility.sendRequest(requestOFS);
		String[] spiltDataOfs = ofsResponse.split(",");
		// String[] firstPart = spiltDataOfs[0].split("/");
		// String statusFlag = firstPart[2];

		return ResponseEntity.status(HttpStatus.OK).body(ofsResponse);

	}

	@RequestMapping(value = "/pacs09/outward", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> rtgsTrasferOutward(@Valid @RequestBody RtgsInfoPacsNineOutward rtgsInfoPacsNineOut,
			HttpServletRequest httpServletRequest) throws Exception {

		String uniqueFtId = rtgsInfoPacsNineOut.getUniqueOutwardRtgsId();
		String coCode = rtgsInfoPacsNineOut.getCoCode();
		String companyCode = rtgsInfoPacsNineOut.getCompanyCode() + coCode;
		rtgsInfoPacsNineOut.setCompanyCode(companyCode);
		String txType = rtgsInfoPacsNineOut.getTxType();
		String debitAccNo = rtgsInfoPacsNineOut.getDebitAccNo();
		String currency = rtgsInfoPacsNineOut.getCurrency();
		String debitAmount = String.format("%.2f", rtgsInfoPacsNineOut.getDebitAmount());
		String creditAccNo = rtgsInfoPacsNineOut.getCreditAccNo();
		String debitDetails = rtgsInfoPacsNineOut.getDebitDetails();
		String creditDetails = rtgsInfoPacsNineOut.getCreditDetails();
		String commissionCode = rtgsInfoPacsNineOut.getCommissionCode();
		String commissionType = rtgsInfoPacsNineOut.getCommissionType();
		String otherInfo = rtgsInfoPacsNineOut.getOtherInfo();
		String billDescription = rtgsInfoPacsNineOut.getBillDescription();
		String lcNumber = rtgsInfoPacsNineOut.getLcNumber();
		String partyName = rtgsInfoPacsNineOut.getPartyName();
		String instructionInfo = rtgsInfoPacsNineOut.getInstructionInfo();
		String tradeFinanceInfo = rtgsInfoPacsNineOut.getTradeFinanceInfo();
		String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		Timestamp t = new Timestamp(new Date().getTime());
		rtgsInfoPacsNineOut.setIssueDate(t);

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
				+ "LOCAL.REF:94:1=" + uniqueFtId +","
				+ "LOCAL.REF:125:1=" + otherInfo +","
				+ "LOCAL.REF:126:1=" + billDescription +","
				+ "LOCAL.REF:127:1=" + lcNumber +","
				+ "LOCAL.REF:128:1=" + partyName +","
				+ "LOCAL.REF:129:1=" + instructionInfo +","
				+ "LOCAL.REF:130:1=" + tradeFinanceInfo;
		
		Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		rtgsInfoPacsNineOut.setHostname(hostIpData.get("host"));
		rtgsInfoPacsNineOut.setIp(hostIpData.get("remoteAddr"));

//		ResponseEntity<?> response = ftHandlerService.handleRtgsOutwardPacsNineTransaction(requestOFS, rtgsInfoPacsNineOut, httpServletRequest);
		ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, rtgsInfoPacsNineOut, uniqueFtId);

		
		System.out.println(response.getBody());
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		return response;

	}

}