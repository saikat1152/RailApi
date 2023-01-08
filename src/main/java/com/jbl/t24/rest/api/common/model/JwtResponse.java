package com.jbl.t24.rest.api.common.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jbl.t24.rest.api.enums.ResponseStatus;

public class JwtResponse {

	private static final long serialVersionUID = -8091879091924046844L;

	private String access_token;
	private String token_type = "bearer";
	private long expires_in;
	private String refresh_token;
	private String client_id;
	private String userName;

	@JsonFormat(pattern = "E, DD MMM Y HH:mm:ss z", timezone = "GMT+06")
	private Date issued;

	@JsonFormat(pattern = "E, DD MMM Y HH:mm:ss z", timezone = "GMT+06")
	private Date expires;
	private String message = ResponseStatus.TWOZ0.getText();
	private int responseCode = ResponseStatus.TWOZ0.getValue();

	public JwtResponse(String access_token, long expires_in, String refresh_token, String client_id, String userName,
			Date issued, Date expires) {
		this.access_token = access_token;
		this.expires_in = expires_in;
		this.refresh_token = refresh_token;
		this.client_id = client_id;
		this.userName = userName;
		this.issued = issued;
		this.expires = expires;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getIssued() {
		return issued;
	}

	public void setIssued(Date issued) {
		this.issued = issued;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponsecode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}