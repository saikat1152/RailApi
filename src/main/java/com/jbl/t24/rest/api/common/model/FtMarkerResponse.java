package com.jbl.t24.rest.api.common.model;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class FtMarkerResponse {

    private HttpStatus status;
	private String message;
	private int responseCode;
	private String accountNumber;
    private String additionalInfo;

	// @JsonFormat(pattern = "E, dd MMM Y HH:mm:ss z", timezone = "GMT+06")
	@JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss", timezone = "GMT+06")
	private Timestamp timestamp = Timestamp.from(Instant.now());

}
