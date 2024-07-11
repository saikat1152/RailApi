package com.jbl.t24.rest.api.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.jbl.t24.rest.api.config.HostIpHandle;
import com.jbl.t24.rest.api.constant.OfsSources;
import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;

import com.jbl.t24.rest.api.model.rtgs.RtgsInfoOutward;
import com.jbl.t24.rest.api.service.rtgs.FtHandlerServiceN;

@RestController
@CrossOrigin
@RequestMapping("/rtgs_out")
@Validated
public class RtgsOutwardTransaction {

	@Autowired
	private FtHandlerServiceN ftHandlerServicen;

	/**
	 * BEFT , RTGS, BACH Current Timestamp YYYYMMDD HH:MM:SS Unique ID
	 */

	/**
	 * Currency USD, GBP, EUR,
	 */
	/**
	 * ACOD --> BEFTN Outward ACOR ---> RTGS Outward ACIR ---> RTGS Inward ACOP --->
	 * RTGS Outward PACS 09 FC ACIN ---> RTGS Inward PACS 09 FC
	 */

	@PostMapping(value = "/outward", consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> rtgsTrasferOutward(@Valid @RequestBody RtgsInfoOutward rtgsInfoOut,
			HttpServletRequest httpServletRequest) throws Exception {

		String uniqueFtId = rtgsInfoOut.getUniqueOutwardRtgsId();
		String coCode = rtgsInfoOut.getCoCode();
		String companyCode = rtgsInfoOut.getCompanyCode() + coCode;
		rtgsInfoOut.setCompanyCode(companyCode);
		String txCategory = rtgsInfoOut.getTxCategory();
		String txType = RtgsTransactionConstants.rtgsConstants.get(txCategory).getTransactionType();
		rtgsInfoOut.setTxType(txType);
		rtgsInfoOut.setCategoryCode(RtgsTransactionConstants.rtgsConstants.get(txCategory).getCode());
		String debitAccNo = rtgsInfoOut.getDebitAccNo();
		String currency = rtgsInfoOut.getCurrency();
		// String debitAmount = String.format("%.2f", rtgsInfoOut.getDebitAmount());
		String debitAmount = rtgsInfoOut.getDebitAmountStr();
		String creditAccNo = rtgsInfoOut.getCreditAccNo();
		String debitDetails = rtgsInfoOut.getDebitDetails();
		String creditDetails = rtgsInfoOut.getCreditDetails();
		String commissionCode = rtgsInfoOut.getCommissionCode();
		String commissionType = rtgsInfoOut.getCommissionType();
		boolean isFc = rtgsInfoOut.getCurrency().contains("BDT") ? false : true;

		rtgsInfoOut.setIsFc(isFc);
		String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		Timestamp t = new Timestamp(new Date().getTime());
		rtgsInfoOut.setIssueDate(t);

		Objects.requireNonNull(uniqueFtId);
		Objects.requireNonNull(coCode);
		Objects.requireNonNull(debitAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(currency);

		if (rtgsInfoOut.getReverseEnabled() != null) {
			rtgsInfoOut.setReverseEnabled(true);
		}

		// String requestOFS = "";

		issueDate = "20220506";
		/*
		 * final String requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0," +
		 * companyCode + ",,TRANSACTION.TYPE=" + txType + "," + "DEBIT.ACCT.NO=" +
		 * debitAccNo + "," + "DEBIT.CURRENCY=" + currency + "," + "DEBIT.AMOUNT=" +
		 * debitAmount + "," + "DEBIT.VALUE.DATE=" + issueDate + "," + "CREDIT.ACCT.NO="
		 * + creditAccNo + "," + "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1," +
		 * "FT.DR.DETAILS=" + debitDetails + "," + "FT.CR.DETAILS=" + creditDetails +
		 * "," + "COMMISSION.CODE=" + commissionCode+ "," + "COMMISSION.TYPE="+
		 * commissionType +"," + "CHEQUE.NUMBER=," + "LOCAL.REF:3:1=," +
		 * "LOCAL.REF:94:1=" + uniqueFtId;
		 */

		final String requestOFS = String.format(OfsSources.REQUEST_OFS_STRING, companyCode, txType, debitAccNo,
				currency, debitAmount, issueDate, creditAccNo, debitDetails, creditDetails, commissionCode,
				commissionType, uniqueFtId);

		Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		rtgsInfoOut.setHostname(hostIpData.get("host"));
		rtgsInfoOut.setIp(hostIpData.get("remoteAddr"));

		// ResponseEntity<?> response =
		// ftHandlerService.handleRtgsOutwardTransaction(requestOFS, rtgsInfoOut,
		// httpServletRequest);
		ResponseEntity<?> response = ftHandlerServicen.handleFtTransaction(requestOFS, rtgsInfoOut, uniqueFtId);

		System.out.println(response.getBody());
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		return response;

	}

}