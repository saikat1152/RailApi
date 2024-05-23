package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementNineOutInfo;

public interface RTGSSettlementNineOutInfoRepository extends JpaRepository<RTGSSettlementNineOutInfo, Long> {

	RTGSSettlementNineOutInfo findByUniqueSettlementtId(String uniqueSettlementtId);

}
