package com.jbl.t24.rest.api.common.model;

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

public class ResponseMsgProcessorWrapper {
	private String message;
    private int responseCode;
    private int ftStatus;
    private String additionalInfo;
    private String ftRef;
    private String uniqueOperationTransactionId;

    public void setWrapperValues(String ftRef, String message, int responseCode, int ftStatus) {
        this.message = message;
        this.ftRef = ftRef;
        this.responseCode = responseCode;
        this.ftStatus = ftStatus;
    }
}
