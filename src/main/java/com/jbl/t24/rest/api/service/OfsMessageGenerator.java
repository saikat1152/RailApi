package com.jbl.t24.rest.api.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.model.CommonFtInfo;
import com.jbl.t24.rest.api.model.EftInfoOutward;
import com.jbl.t24.rest.api.model.SettlementInInfo;
import com.jbl.t24.rest.api.model.SettlementOutInfo;

@Service
public class OfsMessageGenerator {

    public String generatedOfsString(EftInfoOutward eftnInfoOut) {

        String requestOFS = "";

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

        issueDate = "20220506";

        requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0,"
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

        return requestOFS;

    }

    public static String generateOfsMessage(CommonFtInfo ftInfo, String uniqueId) {

        CommonFtInfo settlementInfo;

        if (ftInfo instanceof SettlementInInfo) {
            settlementInfo = new SettlementInInfo();
        } else {
            settlementInfo = new SettlementOutInfo();
        }

        settlementInfo = ftInfo;

        String requestOFS = "";

        // String uniqueFtId = settlementInfo.getUniqueSettlementtId();
        String uniqueFtId = uniqueId;
        String coCode = settlementInfo.getCoCode();
        String companyCode = settlementInfo.getCompanyCode() + coCode;
        settlementInfo.setCompanyCode(companyCode);
        String txType = settlementInfo.getTxType();
        String debitAccNo = settlementInfo.getDebitAccNo();
        String currency = settlementInfo.getCurrency();
        String debitAmount = String.format("%.2f", settlementInfo.getDebitAmount());
        String creditAccNo = settlementInfo.getCreditAccNo();
        String debitDetails = settlementInfo.getDebitDetails();
        String creditDetails = settlementInfo.getCreditDetails();
        String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
        Timestamp t = new Timestamp(new Date().getTime());
        settlementInfo.setIssueDate(t);

        Objects.requireNonNull(uniqueFtId);
        Objects.requireNonNull(coCode);
        Objects.requireNonNull(debitAccNo);
        Objects.requireNonNull(debitAmount);
        Objects.requireNonNull(creditAccNo);
        Objects.requireNonNull(currency);

        issueDate = "20220506";

        requestOFS = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0,"
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

        return requestOFS;
    }

}
