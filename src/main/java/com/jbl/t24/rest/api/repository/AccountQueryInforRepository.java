package com.jbl.t24.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jbl.t24.rest.api.common.model.AccountQueryInfo;

// @Repository
public interface AccountQueryInforRepository extends JpaRepository<AccountQueryInfo ,Integer> {
    
}
