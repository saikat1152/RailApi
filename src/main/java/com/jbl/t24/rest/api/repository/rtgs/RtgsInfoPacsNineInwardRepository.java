package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineInward;

@Repository
public interface RtgsInfoPacsNineInwardRepository extends JpaRepository<RtgsInfoPacsNineInward, Integer> {

	RtgsInfoPacsNineInward findByUniqueInwardRtgsId(String uniqueInwardRtgsId);

}
