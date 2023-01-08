package com.jbl.t24.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbl.t24.rest.api.model.BeftnInwardInfo;

public interface BeftnInwardRepository extends JpaRepository<BeftnInwardInfo, Integer> {
	
	BeftnInwardInfo findByNarrative(String narrative);

}
