package com.jbl.t24.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.jbl.t24.rest.api.model.SettlementOutInfo;

public interface SettlementOutInfoRepositoryEFT extends CrudRepository<SettlementOutInfo, Long> {

     SettlementOutInfo findByUniqueSettlementtId(String uniqueSettlementtId);

}
