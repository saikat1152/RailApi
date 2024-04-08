package com.jbl.t24.rest.api.service.rtgs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.constant.OfsSources;
import com.jbl.t24.rest.api.model.rtgs.CommonFtInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementInInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementOutInfo;

@Service
public class OfsMessageGenerator {

	public String generatedOfsString(RTGSSettlementOutInfo settlementOutInfo) {

		String requestOFS = "";

		String uniqueFtId = settlementOutInfo.getUniqueSettlementtId();
		String coCode = settlementOutInfo.getCoCode();
		String companyCode = settlementOutInfo.getCompanyCode() + coCode;
		settlementOutInfo.setCompanyCode(companyCode);
		String txType = settlementOutInfo.getTxType();
		String debitAccNo = settlementOutInfo.getDebitAccNo();
		String currency = settlementOutInfo.getCurrency();
		String debitAmount = String.format("%.2f", settlementOutInfo.getDebitAmount());
		String creditAccNo = settlementOutInfo.getCreditAccNo();
		String debitDetails = settlementOutInfo.getDebitDetails();
		String creditDetails = settlementOutInfo.getCreditDetails();
		String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		Timestamp t = new Timestamp(new Date().getTime());
		settlementOutInfo.setIssueDate(t);

		Objects.requireNonNull(uniqueFtId);
		Objects.requireNonNull(coCode);
		Objects.requireNonNull(debitAccNo);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(currency);

		requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0," + companyCode + ",,TRANSACTION.TYPE=" + txType + ","
				+ "DEBIT.ACCT.NO=" + debitAccNo + "," + "DEBIT.CURRENCY=" + currency + "," + "DEBIT.AMOUNT="
				+ debitAmount + "," + "DEBIT.VALUE.DATE=" + issueDate + "," + "CREDIT.ACCT.NO=" + creditAccNo + ","
				+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1," + "FT.DR.DETAILS=" + debitDetails + "," + "FT.CR.DETAILS="
				+ creditDetails + "," + "COMMISSION.TYPE=," + "CHEQUE.NUMBER=," + "LOCAL.REF:3:1=," + "LOCAL.REF:94:1="
				+ uniqueFtId;

		return requestOFS;

	}

	public static String generateOfsMessage(CommonFtInfo ftInfo, String uniqueId) {

		String uniqueFtId = "";

		if (ftInfo instanceof RTGSSettlementInInfo) {
			uniqueFtId = ((RTGSSettlementInInfo) ftInfo).getUniqueSettlementtId();
		} else if (ftInfo instanceof RTGSSettlementOutInfo) {
			uniqueFtId = ((RTGSSettlementOutInfo) ftInfo).getUniqueSettlementtId();
		}
		String requestOFS = "";
		String coCode = ftInfo.getCoCode();
		String companyCode = ftInfo.getCompanyCode() + coCode;
		ftInfo.setCompanyCode(companyCode);
		String txType = ftInfo.getTxType();
		String debitAccNo = ftInfo.getDebitAccNo();
		String currency = ftInfo.getCurrency();
		String debitAmount = String.format("%.2f", ftInfo.getDebitAmount());
		String creditAccNo = ftInfo.getCreditAccNo();
		String debitDetails = ftInfo.getDebitDetails();
		String creditDetails = ftInfo.getCreditDetails();
		String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		Timestamp t = new Timestamp(new Date().getTime());
		ftInfo.setIssueDate(t);

		// * Comment it for later Live deployment*/
		issueDate = "20220506";
		// * Comment it for later Live deployment*/
		Objects.requireNonNull(uniqueFtId);
		Objects.requireNonNull(coCode);
		Objects.requireNonNull(debitAccNo);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(currency);

		requestOFS = String.format(OfsSources.REQUEST_OFS_STRING, companyCode, txType, debitAccNo, currency,
				debitAmount, issueDate, creditAccNo, debitDetails, creditDetails, uniqueFtId);

		System.out.println(requestOFS);

		return requestOFS;
	}

}
