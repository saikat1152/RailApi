package com.jbl.t24.rest.api.enums;

public enum FtStatus {
	PENDING(1, "Pending"), SUCCESS(2, "Success"), FAILED(3, "Failed"), REVERSED(4, "Reversed");

	private final int value;
	private final String text;

	FtStatus(int value, String text) {
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
