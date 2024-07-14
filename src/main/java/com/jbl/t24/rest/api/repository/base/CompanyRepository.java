package com.jbl.t24.rest.api.repository.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.base.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    List<Company> findBycompanyIdGreaterThan(Integer companyId);

    Company findByCompanyId(Integer companyId);

    Company findByCompanyIdNot(Integer companyId);

}

