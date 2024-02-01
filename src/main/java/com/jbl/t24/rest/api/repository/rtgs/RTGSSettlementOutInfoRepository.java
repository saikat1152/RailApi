package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.repository.CrudRepository;

import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementOutInfo;

public interface RTGSSettlementOutInfoRepository extends CrudRepository<RTGSSettlementOutInfo, Long> {

	RTGSSettlementOutInfo findByUniqueSettlementtId(String uniqueSettlementtId);

}
