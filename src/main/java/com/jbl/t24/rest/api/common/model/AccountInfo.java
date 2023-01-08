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
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getLeagcyAccountNo() {
		return leagcyAccountNo;
	}
	public void setLeagcyAccountNo(String leagcyAccountNo) {
		this.leagcyAccountNo = leagcyAccountNo;
	}
	public String getAccountTitle() {
		return accountTitle;
	}
	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}
	public String getCoCode() {
		return coCode;
	}
	public void setCoCode(String coCode) {
		this.coCode = coCode;
	}
	public String getCoName() {
		return coName;
	}
	public void setCoName(String coName) {
		this.coName = coName;
	}
	public String getCusMobNum() {
		return cusMobNum;
	}
	public void setCusMobNum(String cusMobNum) {
		this.cusMobNum = cusMobNum;
	}
	public String getCusNidNum() {
		return cusNidNum;
	}
	public void setCusNidNum(String cusNidNum) {
		this.cusNidNum = cusNidNum;
	}
	public String getAccBalance() {
		return AccBalance;
	}
	public void setAccBalance(String accBalance) {
		AccBalance = accBalance;
	}
	public int getPostingRestrictionCode() {
		return postingRestrictionCode;
	}
	public void setPostingRestrictionCode(int postingRestrictionCode) {
		this.postingRestrictionCode = postingRestrictionCode;
	}
	public String getPostingResTrictionType() {
		return postingResTrictionType;
	}
	public void setPostingResTrictionType(String postingResTrictionType) {
		this.postingResTrictionType = postingResTrictionType;
	}
	public String getPostingRestrictionDescription() {
		return postingRestrictionDescription;
	}
	public void setPostingRestrictionDescription(String postingRestrictionDescription) {
		this.postingRestrictionDescription = postingRestrictionDescription;
	}

}
