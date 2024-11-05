package com.jbl.t24.rest.api.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.model.rtgs.RtgsReconIndividual;
import com.jbl.t24.rest.api.service.rtgs.RtgsInfoService;

@Service
public class TransactionStatusFetcher {

    @Autowired
    RtgsInfoService rtgsInfoService;

    public int getTransactionStatus(RtgsReconIndividual reconIndividual, String txUniqueId) {
        return rtgsInfoService.findByUniqueId(reconIndividual, txUniqueId).getStatus();
    
    }
}
