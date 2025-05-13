package com.jbl.t24.rest.api.config;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
// import com.jbl.t24.rest.api.model.reconcileDtos.GroupReconResponse;

public class Mapper {

	public static String mapToJsonString(Object object) {
		try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static FtTxResponse readValue(String responseStr) {
		try {
			return new ObjectMapper().readValue(responseStr, FtTxResponse.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// public static GroupReconResponse readGroupReconValue(String response){
	// 	try {
	// 		return new ObjectMapper().readValue(response, GroupReconResponse.class);
	// 	} catch (JsonProcessingException e) {
	// 		e.printStackTrace();
	// 	}
	// 	return null;
	// }

	public static JwtErrorResponse readValueJwt(String responseStr) {
		try {
			return new ObjectMapper().readValue(responseStr, JwtErrorResponse.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> readValueForMap(String responseStr) {
		try {
			return new ObjectMapper().readValue(responseStr, HashMap.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
