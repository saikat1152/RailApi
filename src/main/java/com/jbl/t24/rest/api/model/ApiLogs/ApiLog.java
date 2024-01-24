package com.jbl.t24.rest.api.model.ApiLogs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "apilogs")
public class ApiLog {

	public ApiLog() {
		
	}
	
	public ApiLog(String protocol, String ip, String uri, String hostName, String httpRequest) {
		this.protocol = protocol;
		this.ip = ip;
		this.uri = uri;
		this.hostName = hostName;
		this.httpRequest = httpRequest;
		this.requestAt = new Timestamp(System.currentTimeMillis());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "logid", unique = true, nullable = false)
	private int logId;

	@Column(name = "protocol", length = 63)
	private String protocol;

	@Column(name = "ip", length = 63)
	private String ip;

	@Column(name = "uri", length = 127)
	private String uri;

	@Column(name = "hostname", length = 127)
	private String hostName;

	@Column(name = "http_request", columnDefinition = "JSON")
	private String httpRequest;

	@Column(name = "http_response", columnDefinition = "JSON")
	private String httpResponse;

	@Column(name = "request_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
	private Timestamp requestAt;

	@Column(name = "response_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = true)
	private Timestamp responseAt;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(String httpRequest) {
		this.httpRequest = httpRequest;
	}

	public String getHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(String httpResponse) {
		this.httpResponse = httpResponse;
	}

	public Timestamp getRequestAt() {
		return requestAt;
	}

	public void setRequestAt(Timestamp requestAt) {
		this.requestAt = requestAt;
	}

	public Timestamp getResponseAt() {
		return responseAt;
	}

	public void setResponseAt(Timestamp responseAt) {
		this.responseAt = responseAt;
	}

}
