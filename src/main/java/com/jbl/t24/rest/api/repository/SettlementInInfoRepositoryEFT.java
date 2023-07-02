package com.jbl.t24.rest.api.repository;

import com.jbl.t24.rest.api.model.SettlementInInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SettlementInInfoRepositoryEFT extends JpaRepository<SettlementInInfo, Long>{

    SettlementInInfo findByUniqueSettlementtId(String uniqueSettlementtId);

}
