package com.jbl.t24.rest.api.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "jwt_user")

public class JwtUser {

	private static final long serialVersionUID = 5926468583005150707L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "jwt_user_id", unique = true, nullable = false)
	private long jwtUserId;
	@Column(unique = true)
	private String username;

	private String password;

	private String basic_token;

	private Boolean status;

	@ManyToOne(fetch = FetchType.LAZY
    // cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    //     CascadeType.REFRESH }
        )
	@JoinColumn(name = "company_id")
    private Company company;

	@Column(name = "whitelist_ip", length = 255)
	private String whitelistIp;

	public long getJwtUserId() {
		return jwtUserId;
	}

	public void setJwtUserId(long jwtUserId) {
		this.jwtUserId = jwtUserId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	// need default constructor for JSON Parsing
	public JwtUser() {

	}

	public JwtUser(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBasic_token() {
		return basic_token;
	}

	public void setBasic_token(String basic_token) {
		this.basic_token = basic_token;
	}

	public String getWhitelistIp() {
		return whitelistIp;
	}

	public void setWhitelistIp(String whitelistIp) {
		this.whitelistIp = whitelistIp;
	}

	public Company getCompany() {
		return company;
	}
}