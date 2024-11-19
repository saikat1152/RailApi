package com.jbl.t24.rest.api.service.rtgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.model.rtgs.RtgsGroupReconcillation;
import com.jbl.t24.rest.api.repository.rtgs.RtgsReconRepositoryGroup;

@Service
public class RtgsGroupReconService {

    @Autowired
    private RtgsReconRepositoryGroup reconRepositoryGroup;

    public void saveGroupRecon(RtgsGroupReconcillation groupReconcillation){
        reconRepositoryGroup.save(groupReconcillation);
    }

    public RtgsGroupReconcillation GroupReconUniqueId(String reconGroupUniqueID){
        return reconRepositoryGroup.findByReconGroupUniqueID(reconGroupUniqueID);
    }
    
}
