package com.jbl.t24.rest.api.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.jbl.t24.rest.api.config.HostIpHandle;
import com.jbl.t24.rest.api.constant.OfsSources;
import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoInward;
import com.jbl.t24.rest.api.service.rtgs.FtHandlerServiceN;

@RestController
@CrossOrigin
@RequestMapping("/rtgs_in")
@Validated
public class RtgsInwardTransaction {

	@Autowired
	private FtHandlerServiceN ftHandlerServiceN;

	@Value("${use.fixed.issue.date}")
	 private Boolean useFixedIssueDate;
	 @Value("${fixed.issue.date}")
	private String fixedIssueDate;
	@PostMapping(value = "/inward", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rtgsTrasferInward(@Valid @RequestBody RtgsInfoInward rtgsInfoInward,
			HttpServletRequest httpServletRequest) throws Exception {

		String uniqueFtId = rtgsInfoInward.getUniqueInwardRtgsId();
		String coCode = rtgsInfoInward.getCoCode();
		String companyCode = rtgsInfoInward.getCompanyCode() + coCode;
		rtgsInfoInward.setCompanyCode(companyCode);
		String txCategory = rtgsInfoInward.getTxCategory();
		String txType = RtgsTransactionConstants.rtgsConstants.get(txCategory).getTransactionType();
		rtgsInfoInward.setTxType(txType);
		rtgsInfoInward.setCategoryCode(RtgsTransactionConstants.rtgsConstants.get(txCategory).getCode());
		// String txType = rtgsInfoInward.getTxType();
		String debitAccNo = rtgsInfoInward.getDebitAccNo();
		String currency = rtgsInfoInward.getCurrency();
		// String debitAmount = String.format("%.2f", rtgsInfoInward.getDebitAmount());
		String debitAmount = rtgsInfoInward.getDebitAmountStr();
		String creditAccNo = rtgsInfoInward.getCreditAccNo();
		String debitDetails = rtgsInfoInward.getDebitDetails();
		String creditDetails = rtgsInfoInward.getCreditDetails();
		String commissionCode = rtgsInfoInward.getCommissionCode();
		String commissionType = rtgsInfoInward.getCommissionType();
		boolean isFc = rtgsInfoInward.getCurrency().contains("BDT") ? false : true;
		rtgsInfoInward.setIsFc(isFc);
		// String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		// Timestamp t = new Timestamp(new Date().getTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                                                       .withZone(ZoneId.systemDefault());
		String issueDate = formatter.format(Instant.now());													   
		
		// Timestamp t = new Timestamp(new Date().getTime());
		Instant instant = Instant.now();
		Timestamp t =Timestamp.from(instant);
		rtgsInfoInward.setIssueDate(t);

		Objects.requireNonNull(uniqueFtId);
		Objects.requireNonNull(coCode);
		Objects.requireNonNull(debitAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(currency);

		// String requestOFS = "";

		if(useFixedIssueDate && fixedIssueDate != null && !fixedIssueDate.trim().isEmpty())
		{
			issueDate = fixedIssueDate;
		}
		
//		final String requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0,"
//				+ companyCode
//				+ ",,TRANSACTION.TYPE=" + txType + ","
//				+ "DEBIT.ACCT.NO=" + debitAccNo + ","
//				+ "DEBIT.CURRENCY=" + currency + ","
//				+ "DEBIT.AMOUNT=" + debitAmount + ","
//				+ "DEBIT.VALUE.DATE=" + issueDate + ","
//				+ "CREDIT.ACCT.NO=" + creditAccNo + ","
//				+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1,"
//				+ "FT.DR.DETAILS=" + debitDetails + ","
//				+ "FT.CR.DETAILS=" + creditDetails + ","
//				+ "COMMISSION.CODE=" + commissionCode+ ","
//				+ "COMMISSION.TYPE="+ commissionType +","
//				+ "CHEQUE.NUMBER=,"
//				+ "LOCAL.REF:3:1=,"
//				+ "LOCAL.REF:94:1=" + uniqueFtId;

		Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		rtgsInfoInward.setHostname(hostIpData.get("host"));
		rtgsInfoInward.setIp(hostIpData.get("remoteAddr"));

		final String requestOFS = String.format(OfsSources.REQUEST_OFS_STRING, companyCode, txType, debitAccNo,
				currency, debitAmount, issueDate, creditAccNo, debitDetails, creditDetails, commissionCode,
				commissionType, uniqueFtId);

//		ResponseEntity<?> response = ftHandlerService.handleRtgsInwardTransaction(requestOFS, rtgsInfoInward, httpServletRequest);
		ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, rtgsInfoInward, uniqueFtId);

		System.out.println(response.getBody());
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		return response;

	}

}
