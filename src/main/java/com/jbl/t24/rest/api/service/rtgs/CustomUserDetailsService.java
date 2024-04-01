package com.jbl.t24.rest.api.service.rtgs;

import com.jbl.t24.rest.api.model.base.JwtUser;
import com.jbl.t24.rest.api.model.rtgs.MyUserAccountDetails;
import com.jbl.t24.rest.api.repository.base.JwtUserRepository;
//import com.jbl.t24.rest.api.service.Lazy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Lazy
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
