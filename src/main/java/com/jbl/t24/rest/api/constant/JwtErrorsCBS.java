package com.jbl.t24.rest.api.constant;

import org.springframework.http.HttpStatus;

import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.enums.ResponseStatus;

public class JwtErrorsCBS {

	public static JwtErrorResponse getCbsJwtError(String responseData) {
		JwtErrorResponse jwtErrorResponse;
		switch (responseData) {
		case "400":
		case "401":
			jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ3.getText(),
					ResponseStatus.FIVEZ3.getValue());
			break;
		case "402":
			jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ2.getText(),
					ResponseStatus.FIVEZ2.getValue());
			break;
		case "403":
			jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ0.getText(),
					ResponseStatus.FIVEZ0.getValue());
			break;
		case "404":
			jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ4.getText(),
					ResponseStatus.FIVEZ4.getValue());
			break;
		case "405":
			jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ5.getText(),
					ResponseStatus.FIVEZ5.getValue());
			break;
		default:
			jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ99.getText(),
					ResponseStatus.FIVEZ99.getValue());
		}
		return jwtErrorResponse;

	}

}
