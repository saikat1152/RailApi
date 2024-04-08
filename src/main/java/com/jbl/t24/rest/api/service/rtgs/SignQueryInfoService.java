package com.jbl.t24.rest.api.service.rtgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.jbl.t24.rest.api.model.rtgs.SignatureQueryInfo;
import com.jbl.t24.rest.api.repository.rtgs.SignQueryInfoRepository;

@Service
@Transactional(readOnly = true)
public class SignQueryInfoService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private SignQueryInfoRepository SignQueryInfoService;

	@Transactional(readOnly = false)
	public SignatureQueryInfo save(SignatureQueryInfo entity) {
		return SignQueryInfoService.save(entity);
	}

}
