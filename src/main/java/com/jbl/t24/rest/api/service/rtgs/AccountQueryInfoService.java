package com.jbl.t24.rest.api.service.rtgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.jbl.t24.rest.api.model.rtgs.AccountQueryInfo;
import com.jbl.t24.rest.api.repository.rtgs.AccountQueryInforRepository;

@Service
@Transactional(readOnly = true)
public class AccountQueryInfoService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AccountQueryInforRepository accountQueryInforRepository;

	@Transactional(readOnly = false)
	public AccountQueryInfo save(AccountQueryInfo entity) {
		return accountQueryInforRepository.save(entity);
	}

}
