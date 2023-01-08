package com.jbl.t24.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jbl.t24.rest.api.model.JwtUser;

public interface JwtUserRepository extends JpaRepository<JwtUser, Long> {
	JwtUser findByUsername(String username);
}
