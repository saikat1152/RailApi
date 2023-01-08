package com.jbl.t24.rest.api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserAccountDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private JwtUser user;
	private String userName;
	private long userId;
	private String password;
	private boolean status;

	public MyUserAccountDetails(JwtUser user) {
		this.user = user;
		this.userName = user.getUsername();
		this.password = user.getPassword();
		this.userId = user.getJwtUserId();
		this.status = user.getStatus();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return status;
	}

}
