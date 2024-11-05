package com.jbl.t24.rest.api.common.model;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.ToString;

@ToString
public class JwtErrorResponse {

	private HttpStatus status;
	private String message;
	private int responseCode;

	@JsonFormat(pattern ="yy-MM-dd HH:mm:ss")
	private Timestamp timestamp = new Timestamp(new Date().getTime());
	private List<String> errors;

	public JwtErrorResponse(HttpStatus status, String message, int responseCode) {
		this.status = status;
		this.message = message;
		this.responseCode = responseCode;
	}

	public JwtErrorResponse(HttpStatus status, String message, int responseCode, Timestamp timestamp) {
		this.status = status;
		this.message = message;
		this.responseCode = responseCode;
		this.timestamp = timestamp;
	}

	public JwtErrorResponse(HttpStatus status, String message, int responseCode, Timestamp timestamp,
			final List<String> errors) {
		super();
		this.status = status;
		this.message = message;
		this.responseCode = responseCode;
		this.timestamp = timestamp;
		this.errors = errors;
	}

	public JwtErrorResponse(HttpStatus status, String message, int responseCode, Timestamp timestamp,
			final String error) {
		super();
		this.status = status;
		this.message = message;
		this.responseCode = responseCode;
		this.timestamp = timestamp;
		errors = Arrays.asList(error);
	}

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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
