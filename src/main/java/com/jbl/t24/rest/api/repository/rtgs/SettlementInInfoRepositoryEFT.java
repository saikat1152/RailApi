package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.jbl.t24.rest.api.model.rtgs.SettlementInInfo;

public interface SettlementInInfoRepositoryEFT extends JpaRepository<SettlementInInfo, Long>{

    SettlementInInfo findByUniqueSettlementtId(String uniqueSettlementtId);

}
