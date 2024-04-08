package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementInInfo;

public interface RTGSSettlementInInfoRepository extends JpaRepository<RTGSSettlementInInfo, Long> {

	RTGSSettlementInInfo findByUniqueSettlementtId(String uniqueSettlementtId);

}
