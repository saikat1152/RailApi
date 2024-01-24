package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbl.t24.rest.api.model.rtgs.SignatureQueryInfo;

public interface SignQueryInfoRepository extends JpaRepository<SignatureQueryInfo, Integer> {
    
}
