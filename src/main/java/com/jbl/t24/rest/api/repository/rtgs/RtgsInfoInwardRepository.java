package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbl.t24.rest.api.model.rtgs.RtgsInfoInward;


public interface RtgsInfoInwardRepository extends JpaRepository<RtgsInfoInward, Integer> {
    
	RtgsInfoInward findByUniqueInwardRtgsId(String uniqueInwardRtgsId);
    
}