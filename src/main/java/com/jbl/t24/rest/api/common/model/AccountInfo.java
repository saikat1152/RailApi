package com.jbl.t24.rest.api.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {

	private String message;
	private int responseCode;
	private boolean isValid;
	private String accountNo;
	private String leagcyAccountNo;
	private String accountTitle;
	private String coCode;
	private String coName;
	private String cusMobNum;
	private String cusNidNum;
	private String AccBalance;
	private int postingRestrictionCode;
	private String postingResTrictionType;
	private String postingRestrictionDescription;
	private String currency;
	private String accountType;

}