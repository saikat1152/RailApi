package com.jbl.t24.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbl.t24.rest.api.model.RtgsInfoInward;


public interface RtgsInfoInwardRepository extends JpaRepository<RtgsInfoInward, Integer> {
    
	RtgsInfoInward findByUniqueInwardRtgsId(String uniqueInwardRtgsId);
    
}