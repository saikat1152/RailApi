package com.jbl.t24.rest.api.utils;

import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;
import com.jbl.t24.rest.api.model.reconcileDtos.IndividualReconcileDto;
import com.jbl.t24.rest.api.model.rtgs.RtgsReconIndividual;

public class DtoToModelMapper {

    public static RtgsReconIndividual fromIndiDtoToIndivModel(IndividualReconcileDto dto, String coCode){

        return RtgsReconIndividual.builder()
                            .coCode(coCode)
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
                            .build();

    }
    
}
