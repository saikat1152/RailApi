package com.jbl.t24.rest.api.enums.utils;


public class ErrorMessageGenerator {

    public static String getErrorMessage(Exception e) {
        String errorMessage = e.getStackTrace()[0].getFileName() + " " + e.getStackTrace()[0].getLineNumber()
                + " " + e.getMessage();

        return errorMessage;
    }

}
