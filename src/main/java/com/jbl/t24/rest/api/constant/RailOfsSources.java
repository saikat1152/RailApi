package com.jbl.t24.rest.api.constant;

public class RailOfsSources {

	public static final String LOCK_OFS_STRING = "AC.LOCKED.EVENTS,APIINFO/I/PROCESS//0," 
			+ "%s" //Company Code
			+ ",,ACCOUNT.NUMBER=" + "%s"  
			+ "," + "DESCRIPTION=" + "%s" 
			+ "," + "FROM.DATE=" + "%s" 
			+ "," + "TO.DATE=" + "%s" + "," 
			+ "LOCKED.AMOUNT=" + "%s" + "," 
			+ "AT.UNIQUE.ID="+ "%s";    //Unique Id
			
			// + "ORDERING.BANK=JBL" 
			
	public static final String UNLOCK_OFS_STRING = "AC.LOCKED.EVENTS,APIINFO/R/PROCESS//0," 
			+ "%s" + ","
			+ "%s" + ","                //Lock Ref ID 
			+ "AT.UNIQUE.ID="+ "%s";    //Unique Id

			// public static final String UNLOCK_OFS_STRING = "AC.LOCKED.EVENTS,APIINFO/R/PROCESS//0," 
			// + "%s" + ","
			// + "ACLK2432015571" + ","                //Generated Lock ID 
			// + "AT.UNIQUE.ID="+ "2505040000001";    //Unique Id	

	public static final String ACCOUNT_ENQUIRY_STRING ="ENQUIRY.SELECT,,,E.JBL.API,ACCOUNT.NUMBER:EQ=" + 
		    "%s";

	public static final String FUND_TRANSFER_STRING ="FUNDS.TRANSFER,JBL.MOB.INT/I/PROCESS//0,"
	+ "%s"
	+ ",,TRANSACTION.TYPE="+ "%s" +","
	+ "DEBIT.CURRENCY=" + "%s" +","
	+ "DEBIT.ACCT.NO=" + "%s" +","
	+ "CREDIT.ACCT.NO=" +  "%s" +","	
	+ "DEBIT.AMOUNT=" + "%s" +","
	+ "FT.DR.DETAILS=" + "%s" +","
	+ "FT.CR.DETAILS=" + "%s" +","
	+ "CARD.MRK.FT=BD001" + "%s" +","
	+ "AT.UNIQUE.ID="+ "%s" +","
	+ "ORDERING.BANK=JBL" +","
	+ "COMMISSION.CODE:1:1="+ "WAIVE" +","
	+ "CHARGE.CODE:1:1="+ "WAIVE";


	public static final String MARKER_FT_STRING ="JBL.MOB.ACCT.INFO,INPUTREG/I/PROCESS,"
	+ "%s" +","
    + "%s" +","
	+ "ATTRIBUTE5=" +  "%s" +","
	+ "COMPANY.CODE=" + "%s";

}