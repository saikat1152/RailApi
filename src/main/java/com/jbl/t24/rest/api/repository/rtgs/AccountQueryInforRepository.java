package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbl.t24.rest.api.model.rtgs.AccountQueryInfo;

// @Repository
public interface AccountQueryInforRepository extends JpaRepository<AccountQueryInfo, Integer> {

}
