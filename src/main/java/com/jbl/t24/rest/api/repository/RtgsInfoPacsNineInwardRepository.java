package com.jbl.t24.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.RtgsInfoPacsNineInward;

@Repository
public interface RtgsInfoPacsNineInwardRepository extends JpaRepository<RtgsInfoPacsNineInward, Integer> {

	RtgsInfoPacsNineInward findByUniqueInwardRtgsId(String uniqueInwardRtgsId);

}
