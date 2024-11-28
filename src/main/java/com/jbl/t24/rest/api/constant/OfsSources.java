package com.jbl.t24.rest.api.constant;

public class OfsSources {

	public static final String REQUEST_OFS_STRING = "FUNDS.TRANSFER,RTGS.ONLY/I/PROCESS//0," 
			+ "%s" //Company Code
			+ ",,TRANSACTION.TYPE=" + "%s"  
			+ "," + "DEBIT.ACCT.NO=" + "%s" 
			+ "," + "DEBIT.CURRENCY=" + "%s" + ","
			+ "DEBIT.AMOUNT=" + "%s" 
			+ "," + "DEBIT.VALUE.DATE=" + "%s" + "," 
			+ "CREDIT.ACCT.NO=" + "%s" + ","
			+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1," 
			+ "FT.DR.DETAILS=" + "%s" 
			+ "," + "FT.CR.DETAILS=" + "%s" + ","
			+ "COMMISSION.CODE=%s" 
			+ ",COMMISSION.TYPE=%s" 
			+ ",CHEQUE.NUMBER=," 
			+ "LOCAL.REF:3:1=," 
			+ "LOCAL.REF:94:1="+ "%s"; //Unique Id

			public static final String RECON_OFS_STRING = "FUNDS.TRANSFER,RTGS.ONLY/I/PROCESS//0," 
			+ "%s" //Company Code
			+ ",,TRANSACTION.TYPE=" + "%s"  
			+ "," + "DEBIT.ACCT.NO=" + "%s" 
			+ "," + "DEBIT.CURRENCY=" + "%s" + ","
			+ "DEBIT.AMOUNT=" + "%s" 
			+ "," + "DEBIT.VALUE.DATE=" + "%s" + "," 
			+ "CREDIT.ACCT.NO=" + "%s" + ","
			+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1," 
			+ "FT.DR.DETAILS=" + "%s" 
			+ "," + "FT.CR.DETAILS=" + "%s" + ","
			+ "COMMISSION.CODE=%s" 
			+ ",COMMISSION.TYPE=%s" 
			+ ",CHEQUE.NUMBER=," 
			+ "LOCAL.REF:3:1=," 
			+ "LOCAL.REF:94:1="+ "%s" //Unique Id
			+ "," + "CREDIT.CURRENCY=" + "%s,"
			+ "CREDIT.AMOUNT=" + "%s";

			public static final String REQUEST_OFS_STRING_IBAS = "FUNDS.TRANSFER,RTGS.ONLY/I/PROCESS//0," 
			+ "%s" //Company Code
			+ ",,TRANSACTION.TYPE=" + "%s"  
			+ "," + "DEBIT.ACCT.NO=" + "%s" 
			+ "," + "DEBIT.CURRENCY=" + "%s" + ","
			+ "DEBIT.AMOUNT=" + "%s" 
			+ "," + "DEBIT.VALUE.DATE=" + "%s" + "," 
			+ "CREDIT.ACCT.NO=" + "%s" + ","
			+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1," 
			+ "FT.DR.DETAILS=" + "%s" 
			+ "," + "FT.CR.DETAILS=" + "%s" + ","
			+ "COMMISSION.CODE=%s" 
			+ ",COMMISSION.TYPE=%s" 
			+ ",CHEQUE.NUMBER=," 
			+ "LOCAL.REF:3:1=," 
			+ "LOCAL.REF:94:1="+ "%s," // unique id
			+ "LOCAL.REF:131:1=" + "%s"; // ibas id

	public static final String REQUEST_OFS_STRING_P9 = "FUNDS.TRANSFER,RTGS.ONLY/I/PROCESS//0," + "%s"
			+ ",,TRANSACTION.TYPE=" + "%s" + "," + "DEBIT.ACCT.NO=" + "%s" + "," + "DEBIT.CURRENCY=" + "%s" + ","
			+ "DEBIT.AMOUNT=" + "%s" + "," + "DEBIT.VALUE.DATE=" + "%s" + "," + "CREDIT.ACCT.NO=" + "%s" + ","
			+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1," + "FT.DR.DETAILS=" + "%s" + "," + "FT.CR.DETAILS=" + "%s" + ","
			+ "COMMISSION.CODE=" + "%s" + "," + "COMMISSION.TYPE=" + "%s" + "," + "CHEQUE.NUMBER=," + "LOCAL.REF:3:1=,"
			+ "LOCAL.REF:94:1=" + "%s" + "," + "LOCAL.REF:125:1=" + "%s" + "," + "LOCAL.REF:126:1=" + "%s" + ","
			+ "LOCAL.REF:127:1=" + "%s" + "," + "LOCAL.REF:128:1=" + "%s" + "," + "LOCAL.REF:129:1=" + "%s" + ","
			+ "LOCAL.REF:130:1=" + "%s";

			public static final String REQUEST_OFS_STRING_SETTLEMENT = "FUNDS.TRANSFER,RTGS.ONLY/I/PROCESS//0," + "%s"
			+ ",,TRANSACTION.TYPE=" + "%s" + "," + "DEBIT.ACCT.NO=" + "%s" + "," + "DEBIT.CURRENCY=" + "%s" + ","
			+ "DEBIT.AMOUNT=" + "%s" + "," + "DEBIT.VALUE.DATE=" + "%s" + "," + "CREDIT.ACCT.NO=" + "%s" + ","
			+ "ORDERING.BANK=JBL,PROFIT.CENTRE.DEPT=1," + "FT.DR.DETAILS=" + "%s" + "," + "FT.CR.DETAILS=" + "%s" + ","
			+ "COMMISSION.CODE=" + ",COMMISSION.TYPE=" + ",CHEQUE.NUMBER=," + "LOCAL.REF:3:1=," + "LOCAL.REF:94:1="
			+ "%s";

	public static final String OFS_ACC_ENQUIRY = "ENQUIRY.SELECT,,,E.JBL.API.BBR,ACCOUNT.NUMBER:EQ=" + "%s";

	public static final String OFS_SIGN_CHECK_ENQUIRY = "ENQUIRY.SELECT,,,IMAGE.VIEW.SIGN,IMAGE.REFERENCE:EQ=" + "%s";

	// public static final String UNIQUE_ID_ENQ_STRING = "ENQUIRY.SELECT,,,E.JBL.BBR.UNIQUE.ID,JBL.BBR.ID:EQ=" + "%s";
	public static final String UNIQUE_ID_ENQ_STRING = "ENQUIRY.SELECT,,,E.RES.RTGS.UNIQUE.ID,JBL.BBR.ID:EQ=" + "%s";

	public static final String REVERSE_OFS_STRING ="FUNDS.TRANSFER,RTGS.ONLY/R/PROCESS//0,BD001";
}
