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
import org.springframework.web.bind.annotation.*;
import com.jbl.t24.rest.api.config.HostIpHandle;
import com.jbl.t24.rest.api.constant.OfsSources;
import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineInward;
import com.jbl.t24.rest.api.service.rtgs.FtHandlerService;
import com.jbl.t24.rest.api.service.rtgs.FtHandlerServiceN;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@RestController
@CrossOrigin
@RequestMapping("/rtgs_in")
@Validated
public class RtgsInwardPacsNineTransaction {

	@Autowired
	private FtHandlerServiceN ftHandlerServiceN;

	@PostMapping(value = "/pacs09/inward", consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> rtgsTrasferInward(@Valid @RequestBody RtgsInfoPacsNineInward rtgsInfoPacsNineIn,
			HttpServletRequest httpServletRequest) throws Exception {

		String uniqueFtId = rtgsInfoPacsNineIn.getUniqueInwardRtgsId();
		String coCode = rtgsInfoPacsNineIn.getCoCode();
		String companyCode = rtgsInfoPacsNineIn.getCompanyCode() + coCode;
		rtgsInfoPacsNineIn.setCompanyCode(companyCode);
		String txCategory = rtgsInfoPacsNineIn.getTxCategory();
		String txType = RtgsTransactionConstants.rtgsConstants.get(txCategory).getTransactionType();
		rtgsInfoPacsNineIn.setTxType(txType);
		rtgsInfoPacsNineIn.setCategoryCode(RtgsTransactionConstants.rtgsConstants.get(txCategory).getCode());
		String debitAccNo = rtgsInfoPacsNineIn.getDebitAccNo();
		String currency = rtgsInfoPacsNineIn.getCurrency();
		String debitAmount = String.format("%.2f", rtgsInfoPacsNineIn.getDebitAmount());
		String creditAccNo = rtgsInfoPacsNineIn.getCreditAccNo();
		String debitDetails = rtgsInfoPacsNineIn.getDebitDetails();
		String creditDetails = rtgsInfoPacsNineIn.getCreditDetails();
		String commissionCode = rtgsInfoPacsNineIn.getCommissionCode();
		String commissionType = rtgsInfoPacsNineIn.getCommissionType();
		boolean isFc = rtgsInfoPacsNineIn.getCurrency().contains("BDT") ? false : true;
		rtgsInfoPacsNineIn.setIsFc(isFc);
		String otherInfo = rtgsInfoPacsNineIn.getOtherInfo();
		String billDescription = rtgsInfoPacsNineIn.getBillDescription();
		String lcNumber = rtgsInfoPacsNineIn.getLcNumber();
		String partyName = rtgsInfoPacsNineIn.getPartyName();
		String instructionInfo = rtgsInfoPacsNineIn.getInstructionInfo();
		String tradeFinanceInfo = rtgsInfoPacsNineIn.getTradeFinanceInfo();
		String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		Timestamp t = new Timestamp(new Date().getTime());
		rtgsInfoPacsNineIn.setIssueDate(t);

		Objects.requireNonNull(uniqueFtId);
		Objects.requireNonNull(coCode);
		Objects.requireNonNull(debitAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(currency);

		// String requestOFS = "";

		issueDate = "20220506";
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
//				+ "LOCAL.REF:94:1=" + uniqueFtId +","
//				+ "LOCAL.REF:125:1=" + otherInfo +","
//				+ "LOCAL.REF:126:1=" + billDescription +","
//				+ "LOCAL.REF:127:1=" + lcNumber +","
//				+ "LOCAL.REF:128:1=" + partyName +","
//				+ "LOCAL.REF:129:1=" + instructionInfo +","
//				+ "LOCAL.REF:130:1=" + tradeFinanceInfo;

		Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		rtgsInfoPacsNineIn.setHostname(hostIpData.get("host"));
		rtgsInfoPacsNineIn.setIp(hostIpData.get("remoteAddr"));

		final String requestOFS = String.format(OfsSources.REQUEST_OFS_STRING_P9, companyCode, txType, debitAccNo,
				currency, debitAmount, issueDate, creditAccNo, debitDetails, creditDetails, commissionCode,
				commissionType, uniqueFtId, otherInfo, billDescription, lcNumber, partyName, instructionInfo,
				tradeFinanceInfo);

		// ResponseEntity<?> response =
		// ftHandlerService.handleRtgsInwardPacsNineTransaction(requestOFS,
		// rtgsInfoPacsNineIn, httpServletRequest);
		ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, rtgsInfoPacsNineIn, uniqueFtId);

		System.out.println(response.getBody());
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		return response;

	}

}
