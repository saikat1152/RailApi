package com.jbl.t24.rest.api.common.model;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FtResponse {

	private HttpStatus status;
	private String message;
	private int responseCode;
	private String narrative;
	private String ftRef;
//	private int creditAccountCategory;
	private String additionalInfo;

	@JsonFormat(pattern ="yy-MM-dd HH:mm:ss", timezone = "GMT+06")
	private Timestamp timestamp = new Timestamp(new Date().getTime());

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
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

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public String getFtRef() {
		return ftRef;
	}

	public void setFtRef(String ftRef) {
		this.ftRef = ftRef;
	}

//	public int getCreditAccountCategory() {
//		return creditAccountCategory;
//	}
//
//	public void setCreditAccountCategory(int creditAccountCategory) {
//		this.creditAccountCategory = creditAccountCategory;
//	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
