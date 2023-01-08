package com.jbl.t24.rest.api.enums;

public enum EftStatus {
    PENDING(1, "Pending"), 
	SUCCESS(2, "Success"), 
	FAILED(3, "Failed");

	private final int value;
	private final String text;

	EftStatus(int value, String text) {
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
