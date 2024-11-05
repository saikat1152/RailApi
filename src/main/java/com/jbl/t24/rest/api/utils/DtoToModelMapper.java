package com.jbl.t24.rest.api.utils;

import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;
import com.jbl.t24.rest.api.model.reconcileDtos.GroupReconcileDto;
import com.jbl.t24.rest.api.model.reconcileDtos.IndividualReconcileDto;
import com.jbl.t24.rest.api.model.rtgs.RtgsGroupReconcillation;
import com.jbl.t24.rest.api.model.rtgs.RtgsReconIndividual;

public class DtoToModelMapper {

    public static RtgsReconIndividual fromIndiDtoToIndivModel(IndividualReconcileDto dto,
            RtgsGroupReconcillation groupRecon) {

        return RtgsReconIndividual.builder()
                .reconGroup(groupRecon)
                .coCode(dto.coCode())
                .creditAccNo(dto.creditAccount())
                .creditAmountStr(dto.creditAmount())
                .creditCurrency(dto.creditCurrency())
                .debitAccNo(dto.debitAccount())
                .debitAmountStr(dto.debitAmount())
                .debitCurrency(dto.debitCurrency())
                .txType(RtgsTransactionConstants.rtgsConstants.get("RECON").getTransactionType())
                .txCategory("RECON")
                .categoryCode(RtgsTransactionConstants.rtgsConstants.get("RECON").getCode())
                .commissionCode("WAIVE")
                .commissionType("")
                .inidvidualReconUniqueId(dto.inidvidualReconUniqueId())
                .currency("BDT")
                .build();

    }

    public static RtgsGroupReconcillation fromReconGroupDtoToReconModel(GroupReconcileDto groupReconcileDto) {

        return RtgsGroupReconcillation.builder()
                .reconGroupUniqueID(groupReconcileDto.groupReconUniqueId())
                .status(1)
                .build();
    }

}
