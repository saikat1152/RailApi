package com.jbl.t24.rest.api.enums;

public enum AccountNature {

	PERSONAL(1, "Personal"),
	OTHER(2, "Others");

	private final int value;
	private final String text;

	AccountNature(int value, String text) {
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
