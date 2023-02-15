package com.jbl.t24.rest.api.enums;

public enum CBSResponseStr {

	alreadySuccessDuplicate("ALREADY.SUCCESS.DUPLICATE.AT.UNIQUE.ID"), 
	failedDuplicate("FAILED.DUPLICATE.AT.UNIQUE.ID"),
	debitAccountMissing("DEBIT.ACCT.NO:1:1=MISSING ACCOUNT - RECORD"),
	creditAccountMissing("CREDIT.ACCT.NO:1:1=MISSING ACCOUNT - RECORD"),
	debitAccountLessBalance("POSITION.TYPE:1:1=WITHDRAWL MAKES A/C BAL LESS THAN MIN BAL"),
	accountNaturePersonal("FRD.AC.NATURE=Personal"),
	customDebitAccountMissing("CUSTOM.DEBIT.ACCOUNT.MISSING"),
	customCreditAccountMissing("CUSTOM.CREDIT.ACCOUNT.MISSING"),
	customDebitAccountCoCodeMissing("CUSTOM.DEBIT.CO.CODE.MISSING"),

	/**
	 * Below response are for BEFTN Purposes
	*/
	unauthorizedOverdraft("Unauthorised Overdraft"),
	postingRestriction("Posting Restriction"),
	valueAmountZero("VAL.AMOUNT.SHOULD.NOT.BE.ZERO"),
	invalidMinus("INVALID MINUS"),

	/**
	 * Below response are for RTGS Purposes
	*/
	invalidCompany("INVALID COMPANY SPECIFIED DURING SIGN ON PROCESS");

	private final String text;

	CBSResponseStr(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
