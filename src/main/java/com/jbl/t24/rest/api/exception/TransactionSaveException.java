package com.jbl.t24.rest.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionSaveException extends RuntimeException {

    private final String error;
    private final String constraint;
    private final String sqlState;
    private final String details;
    
}
