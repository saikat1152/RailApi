package com.jbl.t24.rest.api.service.rtgs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.config.HostIpHandle;
import com.jbl.t24.rest.api.constant.OfsSources;
import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementInInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementNineInInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementNineOutInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementOutInfo;
import com.jbl.t24.rest.api.model.rtgs.RtgsCommon;

@Service
public class OfsMessageGenerator {

	public String generatedOfsString(RTGSSettlementOutInfo settlementOutInfo) {

		String requestOFS = "";

		String uniqueFtId = settlementOutInfo.getUniqueSettlementtId();
		String coCode = settlementOutInfo.getCoCode();
		String companyCode = settlementOutInfo.getCompanyCode() + coCode;
		settlementOutInfo.setCompanyCode(companyCode);
		String txType = settlementOutInfo.getTxType();
		String debitAccNo = settlementOutInfo.getDebitAccNo().trim();
		String currency = settlementOutInfo.getCurrency();
		String debitAmount = String.format("%.2f", settlementOutInfo.getDebitAmount()).trim();
		String creditAccNo = settlementOutInfo.getCreditAccNo().trim();
		String debitDetails = settlementOutInfo.getDebitDetails().trim();
		String creditDetails = settlementOutInfo.getCreditDetails().trim();
		// String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		// Timestamp t = new Timestamp(new Date().getTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                                                       .withZone(ZoneId.systemDefault());
		String issueDate = formatter.format(Instant.now());													   
		
		// Timestamp t = new Timestamp(new Date().getTime());
		Instant instant = Instant.now();
		Timestamp t =Timestamp.from(instant);
		settlementOutInfo.setIssueDate(t);

		Objects.requireNonNull(uniqueFtId);
		Objects.requireNonNull(coCode);
		Objects.requireNonNull(debitAccNo);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(currency);

		requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0," 
				+ companyCode 
				+ ",,TRANSACTION.TYPE=" + txType + ","
				+ "DEBIT.ACCT.NO=" + debitAccNo + "," 
				+ "DEBIT.CURRENCY=" + currency 
				+ "," + "DEBIT.AMOUNT="+ debitAmount
				+ "," + "DEBIT.VALUE.DATE=" + issueDate 
				+ "," + "CREDIT.ACCT.NO=" + creditAccNo + ","
				+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1," 
				+ "FT.DR.DETAILS=" + debitDetails 
				+ "," + "FT.CR.DETAILS="+ creditDetails + "," 
				+ "COMMISSION.TYPE=," 
				+ "CHEQUE.NUMBER=," 
				+ "LOCAL.REF:3:1=," 
				+ "LOCAL.REF:94:1="+ uniqueFtId;

		return requestOFS;

	}

	public String generateOfsMessage(RtgsCommon ftInfo, String uniqueId, HttpServletRequest httpServletRequest,
	Boolean useFixedIssueDate, String fixedIssueDate) 
	{
		String uniqueFtId = "";

		if (ftInfo instanceof RTGSSettlementInInfo) {
			uniqueFtId = ((RTGSSettlementInInfo) ftInfo).getUniqueSettlementtId();
		} else if (ftInfo instanceof RTGSSettlementOutInfo) {
			uniqueFtId = ((RTGSSettlementOutInfo) ftInfo).getUniqueSettlementtId();
		} else if (ftInfo instanceof RTGSSettlementNineInInfo) {
			uniqueFtId = ((RTGSSettlementNineInInfo) ftInfo).getUniqueSettlementtId();
		} else if (ftInfo instanceof RTGSSettlementNineOutInfo) {
			uniqueFtId = ((RTGSSettlementNineOutInfo) ftInfo).getUniqueSettlementtId();
		}
		String requestOFS = "";
		String coCode = ftInfo.getCoCode();
		String companyCode = ftInfo.getCompanyCode() + coCode;
		ftInfo.setCompanyCode(companyCode);
		// String txType = ftInfo.getTxType();
		String debitAccNo = ftInfo.getDebitAccNo().trim();
		String currency = ftInfo.getCurrency();
		// String debitAmount = String.format("%.2f", ftInfo.getDebitAmount());
		String debitAmount = ftInfo.getDebitAmountStr().trim();
		String creditAccNo = ftInfo.getCreditAccNo().trim();
		String debitDetails = ftInfo.getDebitDetails().trim();
		String creditDetails = ftInfo.getCreditDetails().trim();

		ftInfo.setDebitAmountStr(debitAmount);
		ftInfo.setDebitAccNo(debitAccNo);
		ftInfo.setCreditAccNo(creditAccNo);
		ftInfo.setDebitDetails(debitDetails);
		ftInfo.setCreditDetails(creditDetails);

		
		// String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                                                       .withZone(ZoneId.systemDefault());
		String issueDate = formatter.format(Instant.now());	
		// Timestamp t = new Timestamp(new Date().getTime());
		Instant instant = Instant.now();
		Timestamp t =Timestamp.from(instant);
		ftInfo.setIssueDate(t);

		String txCategory = ftInfo.getTxCategory();
		String txType = RtgsTransactionConstants.rtgsConstants.get(txCategory).getTransactionType();
		ftInfo.setTxType(txType);
		ftInfo.setCategoryCode(RtgsTransactionConstants.rtgsConstants.get(txCategory).getCode());

		// * Comment it for later Live deployment*/
		if(useFixedIssueDate && fixedIssueDate != null)
		{
			issueDate = fixedIssueDate;
		}
		
		// * Comment it for later Live deployment*/
		Objects.requireNonNull(uniqueFtId);
		Objects.requireNonNull(coCode);
		Objects.requireNonNull(debitAccNo);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(currency);

		Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		ftInfo.setHostname(hostIpData.get("host"));
		ftInfo.setIp(hostIpData.get("remoteAddr"));

		requestOFS = String.format(OfsSources.REQUEST_OFS_STRING, companyCode, txType, debitAccNo, currency,
				debitAmount, issueDate, creditAccNo, debitDetails, creditDetails,"","",uniqueFtId);

		System.out.println(requestOFS);

		return requestOFS;
	}

}
