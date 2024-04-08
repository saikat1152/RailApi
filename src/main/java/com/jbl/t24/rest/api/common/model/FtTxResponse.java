package com.jbl.t24.rest.api.common.model;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder

public class FtTxResponse {

	private HttpStatus status;
	private String message;
	private int responseCode;
	private String uniqueRTGS;
	private String ftRef;
	private String additionalInfo;

	@JsonFormat(pattern = "E, dd MMM Y HH:mm:ss z", timezone = "GMT+06")
	private Timestamp timestamp = new Timestamp(new Date().getTime());

	@JsonFormat(pattern = "E, dd MMM Y HH:mm:ss z", timezone = "GMT+06")
	private Timestamp reverseTimestamp;
}
