package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.repository.CrudRepository;

import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementNineOutInfo;

public interface RTGSSettlementNineOutInfoRepository extends CrudRepository<RTGSSettlementNineOutInfo, Long> {

	RTGSSettlementNineOutInfo findByUniqueSettlementtId(String uniqueSettlementtId);

}
