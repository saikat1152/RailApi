package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.ApiLogs.ApiLog;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Integer> {

}
