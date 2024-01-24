package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbl.t24.rest.api.model.rtgs.RtgsInfoOutward;

public interface RtgsInfoOutwardRepository extends JpaRepository<RtgsInfoOutward, Integer> {

	RtgsInfoOutward findByUniqueOutwardRtgsId(String uniqueOutwardRtgsId);
	RtgsInfoOutward findByCbsFtno(String cbsFtno);

}