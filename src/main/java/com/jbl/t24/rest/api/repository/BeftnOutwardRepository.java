package com.jbl.t24.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbl.t24.rest.api.model.BeftnOutwardInfo;

public interface BeftnOutwardRepository extends JpaRepository<BeftnOutwardInfo, Integer> {
	
	BeftnOutwardInfo findByCreditNarrative(String creditNarrative);
	BeftnOutwardInfo findByDebitNarrative(String debittNarrative);
	Boolean existsByCreditNarrative(String creditNarrative);



}
