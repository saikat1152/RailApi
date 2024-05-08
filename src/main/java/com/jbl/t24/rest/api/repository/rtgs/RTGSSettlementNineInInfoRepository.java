package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementNineInInfo;

public interface RTGSSettlementNineInInfoRepository extends JpaRepository<RTGSSettlementNineInInfo, Long> {

	RTGSSettlementNineInInfo findByUniqueSettlementtId(String uniqueSettlementtId);

}
