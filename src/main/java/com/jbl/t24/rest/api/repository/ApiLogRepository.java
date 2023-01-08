package com.jbl.t24.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.ApiLog;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Integer> {

}
