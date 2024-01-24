package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineOutward;

@Repository
public interface RtgsInfoPacsNineOutwardRepository extends JpaRepository<RtgsInfoPacsNineOutward, Integer> {

	RtgsInfoPacsNineOutward findByUniqueOutwardRtgsId(String uniqueOutwardRtgsId);

	RtgsInfoPacsNineOutward findByCbsFtno(String findByCbsFtno);

	
}
