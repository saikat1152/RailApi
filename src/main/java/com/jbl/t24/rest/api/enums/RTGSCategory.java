package com.jbl.t24.rest.api.enums;

public enum RTGSCategory {

	RTGSOUTWARDPACS8(1, "RTGS Outward Pacs 08 version"),
	RTGSINWARDPACS8(2, "RTGS Inward Pacs 08 version"),
	CUSTOMSEPAYMENT(3, "Customs E Payment");

	private final int value;
	private final String text;

	private RTGSCategory(int value, String text) {
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