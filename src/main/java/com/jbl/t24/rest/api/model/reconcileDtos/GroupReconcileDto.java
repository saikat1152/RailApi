package com.jbl.t24.rest.api.model.reconcileDtos;

import java.util.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record GroupReconcileDto(
    @NotNull(message = "Group Unique Id for Reconcilation is Required!")
    String groupReconUniqueId,
    @Valid
    List<IndividualReconcileDto> individualReconcileDtos,
    @NotNull(message = "Reconciliation Category is Required!")
    @Pattern(regexp = "in|out", message = "Reconciliation Category must be either 'in' or 'out'")
    String reconCatergory
) {

}
