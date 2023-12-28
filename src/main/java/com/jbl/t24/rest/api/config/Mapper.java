package com.jbl.t24.rest.api.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;

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
    
    public static JwtErrorResponse readValueJwt(String responseStr) {
        try {
            return new ObjectMapper().readValue(responseStr, JwtErrorResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
