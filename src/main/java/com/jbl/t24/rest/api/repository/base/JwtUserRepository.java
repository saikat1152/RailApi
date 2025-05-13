package com.jbl.t24.rest.api.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.base.JwtUser;

@Repository
public interface JwtUserRepository extends JpaRepository<JwtUser, Long> {
	JwtUser findByUsername(String username);
}
