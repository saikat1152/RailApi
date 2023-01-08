package com.jbl.t24.rest.api.common.model;

public class AccountInfoNotFound {

	private String message;
	private int responseCode;
	private boolean isValid;

	public AccountInfoNotFound(String message, int responseCode, boolean isValid) {
		this.message = message;
		this.responseCode = responseCode;
		this.isValid = isValid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
