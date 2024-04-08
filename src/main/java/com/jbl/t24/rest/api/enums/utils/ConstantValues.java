package com.jbl.t24.rest.api.enums.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConstantValues {
	private String transactionType;
	private int code;

}
