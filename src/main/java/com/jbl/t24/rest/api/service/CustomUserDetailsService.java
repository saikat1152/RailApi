package com.jbl.t24.rest.api.service;

import com.jbl.t24.rest.api.model.MyUserAccountDetails;
import com.jbl.t24.rest.api.model.JwtUser;
import com.jbl.t24.rest.api.repository.JwtUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private JwtUserRepository jwtUserRepository;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		JwtUser user = jwtUserRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Not found: " + username);
		}

		return new MyUserAccountDetails(user);
	}

}
