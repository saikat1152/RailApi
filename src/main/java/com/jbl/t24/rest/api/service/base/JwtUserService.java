package com.jbl.t24.rest.api.service.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbl.t24.rest.api.model.base.JwtUser;
import com.jbl.t24.rest.api.repository.base.JwtUserRepository;

@Service
@Transactional(readOnly = true)
public class JwtUserService {

	@Autowired
	private JwtUserRepository jwtuserRepository;

	public List<JwtUser> findAll() {
		return jwtuserRepository.findAll();
	}

	public JwtUser findOne(Long id) {
		return jwtuserRepository.getById(id);
	}

	@Transactional(readOnly = false)
	public JwtUser save(JwtUser entity) {
		return jwtuserRepository.save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(JwtUser entity) {
		jwtuserRepository.delete(entity);
	}

	public JwtUser findByUserName(String userName) {
		return jwtuserRepository.findByUsername(userName);
	}

}
