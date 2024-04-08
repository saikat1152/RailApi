package com.jbl.t24.rest.api.constant;

public class RtgsTruncateString {

	public static String truncateString(String input, int maxLength) {
		if (input.length() > maxLength) {
			return input.substring(0, maxLength);
		} else {
			return input;
		}
	}
}
