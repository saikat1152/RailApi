package com.jbl.t24.rest.api.model.reconcileDtos;

import javax.validation.constraints.NotNull;

import lombok.Data;


public record IndividualReconcileDto(
    String debitAmount,
    String debitAccount,
    String creditAmount,
    String creditAccount,
    String debitCurrency,
    String creditCurrency,
    @NotNull(message = "Individual Unique Id for Reconcilation is Required!")
    String inidvidualReconUniqueId
) {
    
}
