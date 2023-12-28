package com.jbl.t24.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbl.t24.rest.api.model.RtgsInfoOutward;

public interface RtgsInfoOutwardRepository extends JpaRepository<RtgsInfoOutward, Integer> {
    
	RtgsInfoOutward findByUniqueOutwardRtgsId(String uniqueOutwardRtgsId);
	RtgsInfoOutward findByCbsFtno(String cbsFtno);
    
}