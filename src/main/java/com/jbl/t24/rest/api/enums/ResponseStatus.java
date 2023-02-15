package com.jbl.t24.rest.api.enums;

public enum ResponseStatus {

	TWOZ0(200, "Successfully Processed!"),
	TWOZ1(201, "Transaction Failed!"),
	TWOZ2(202, "This Transaction Already Successful! Duplicate BEFTN Narrative!"),
	TWOZ3(203, "Transaction Failed! Duplicate BEFTN Narrative!"),
	TWOZ4(204, "Transaction Failed! Duplicate Credit Narrative!"),
	
	FOURZ0(400, "Invalid Input Parameter Request!"), 
	FOURZ1(401, "Username or Password doesn't match!"),
	FOURZ2(402, "Authorization Key/Token Mismatch!"),
	FOURZ3(403, "Token Expired!"),
	FOURZ4(404, "The TOKEN is not valid!"),
	FOURZ5(405, "Method Not Allowed!"),
	FOURZ6(406, "Please check authorization header value! Missing value or Mismatch format!"),
	FOURZ7(407, "Credit A/C number mismatch or not found!"),
	FOURZ8(408, "Credit A/C number can't be empty!"),
	FOURZ9(409, "Length Does Not Match!"),
	FOURZ10(410, "Cover A/C number missing or not found!"),
	FOURZ11(411, "Cover A/C Branch Code Not Match!"),
	FOURZ12(412, "Account Length Does Not Match!"),
	FOURZ13(413, "Account Number is Not Active!"),
	FOURZ14(414, "Cover Account Does Not Have Minimum Balance!"),
	FOURZ15(415, "Unsupported Media Type/Wrong Input Parameter!"),
	FOURZ16(416, "Required Request Body is Missing!"),
	FOURZ17(417, "Request IP Address is Not Allowed For This User!"),
	FOURZ18(418, "Mandatory Field Can't be Empty or Null!"),
	FOURZ19(419, "User is Disabled! Contact With System Administrator!"),
	FOURZ20(420, "Version Is Not Valid!"),
	//below added newly
	FOURZ21(421, "Unathorized Overdraft!"),
	FOURZ22(422, "Posting Restriction!"),
	FOURZ23(423, "Amount Cannot be Negative!"),
	FOURZ24(424, "Ammount Cannot be Zero!"),
	FOURZ25(425,"Image Not Found!"),
	FOURZ26(426,"Amount cannot be less than 100000 for RTGS"),
	FOURZ27(427,"Invalid Company Code Specified"),

	FIVEZ0(500, "Internal Server Error!"),
	FIVEZ3(503, "Service Unavailable!"),
	FIVEZ4(504, "Response NONE Due to Unresponsive CBS!");

		
	private final int value;
	private final String text;
	
	ResponseStatus(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
	
}
