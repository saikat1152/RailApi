package com.jbl.t24.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jbl.t24.rest.api.common.model.SignatureQueryInfo;

public interface SignQueryInfoRepository extends JpaRepository<SignatureQueryInfo, Integer> {
    
}
