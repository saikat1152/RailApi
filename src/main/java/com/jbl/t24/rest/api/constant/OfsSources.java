package com.jbl.t24.rest.api.constant;

public class OfsSources {
	
	public static final String REQUEST_OFS_STRING = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0,"
			+ "%s"
			+ ",,TRANSACTION.TYPE=" + "%s" + ","
			+ "DEBIT.ACCT.NO=" + "%s" + ","
			+ "DEBIT.CURRENCY=" + "%s" + ","
			+ "DEBIT.AMOUNT=" + "%s" + ","
			+ "DEBIT.VALUE.DATE=" + "%s" + ","
			+ "CREDIT.ACCT.NO=" + "%s" + ","
			+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1,"
			+ "FT.DR.DETAILS=" + "%s" + ","
			+ "FT.CR.DETAILS=" + "%s" + ","
			+ "COMMISSION.CODE=,"
			+ "COMMISSION.TYPE=,"
			+ "CHEQUE.NUMBER=,"
			+ "LOCAL.REF:3:1=,"
			+ "LOCAL.REF:94:1=" + "%s";
	
	
	public static final String REQUEST_OFS_STRING_P9 = "FUNDS.TRANSFER,BACH.EFT.RTGS/I/PROCESS//0,"
			+ "%s"
			+ ",,TRANSACTION.TYPE=" + "%s" + ","
			+ "DEBIT.ACCT.NO=" + "%s" + ","
			+ "DEBIT.CURRENCY=" + "%s" + ","
			+ "DEBIT.AMOUNT=" + "%s" + ","
			+ "DEBIT.VALUE.DATE=" + "%s" + ","
			+ "CREDIT.ACCT.NO=" + "%s" + ","
			+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1,"
			+ "FT.DR.DETAILS=" + "%s" + ","
			+ "FT.CR.DETAILS=" + "%s" + ","
			+ "COMMISSION.CODE=,"
			+ "COMMISSION.TYPE=,"
			+ "CHEQUE.NUMBER=,"
			+ "LOCAL.REF:3:1=,"
			+ "LOCAL.REF:94:1=" + "%s" +","
			+ "LOCAL.REF:125:1=" + "%s" +","
			+ "LOCAL.REF:126:1=" + "%s" +","
			+ "LOCAL.REF:127:1=" + "%s" +","
			+ "LOCAL.REF:128:1=" + "%s" +","
			+ "LOCAL.REF:129:1=" + "%s" +","
			+ "LOCAL.REF:130:1=" + "%s";

	public static final String OFS_ACC_ENQUIRY = "ENQUIRY.SELECT,,,E.JBL.API.BBR,ACCOUNT.NUMBER:EQ=" + "%s";

	public static final String OFS_SIGN_CHECK_ENQUIRY = "ENQUIRY.SELECT,,,IMAGE.VIEW.SIGN,IMAGE.REFERENCE:EQ=" + "%s";
}
