package com.jbl.t24.rest.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbl.t24.rest.api.model.ApiLog;
import com.jbl.t24.rest.api.repository.ApiLogRepository;

@Service
@Transactional(readOnly = true)
public class ApiLogService {

	@Autowired
	private ApiLogRepository apiLogRepository;

	public ApiLog save(ApiLog apiLog) {
		return apiLogRepository.save(apiLog);
	}

}
