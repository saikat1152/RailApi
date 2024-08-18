package com.jbl.t24.rest.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.jbl.t24.rest.api.common.model.ApiToken;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.JwtResponse;
import com.jbl.t24.rest.api.config.JwtTokenUtil;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.ApiLogs.ApiLog;
import com.jbl.t24.rest.api.model.base.JwtUser;
import com.jbl.t24.rest.api.service.base.JwtUserService;
import com.jbl.t24.rest.api.service.rtgs.ApiLogService;
import com.jbl.t24.rest.api.service.rtgs.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.impl.DefaultClaims;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	private JwtUserService jwtUserService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private ApiLogService apiLogService;

	private final ConcurrentHashMap<String, String> tokenCache = new ConcurrentHashMap<>();

	@PostMapping("/api/token")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody ApiToken apiToken,
			HttpServletRequest httpServletRequest) throws Exception {

		// validate input field first

		String grant_type = apiToken.getJbl_core_api_grant_type();
		String userName = apiToken.getJbl_core_api_username();
		String password = apiToken.getJbl_core_api_password();

		// get remote IP address

		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
		if (remoteAddr == null) {
			remoteAddr = httpServletRequest.getRemoteAddr();
		}

		// save log data

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", userName);
		jsonObject.put("grant_type", grant_type);

		String protocol = httpServletRequest.getProtocol();
		String uri = UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(httpServletRequest)).build()
				.toUriString();
		String host = httpServletRequest.getRemoteHost();

		ApiLog apiLog = new ApiLog(protocol, remoteAddr, uri, host, jsonObject.toString());
		apiLogService.save(apiLog);

		// check required value

		Objects.requireNonNull(grant_type);
		Objects.requireNonNull(userName);
		Objects.requireNonNull(password);

		// check request IP address is authorize or not

		boolean status = this.checkRequestIP(userName, remoteAddr);
		if (!status) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
					ResponseStatus.FOURZ17.getText(), ResponseStatus.FOURZ17.getValue());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		}

		// validate authorization header first

		String basicToken = null;
		final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Basic ")) {
			basicToken = requestTokenHeader.substring(6);

			JwtUser jwtUser = jwtUserService.findByUserName(userName);
			String companyName = jwtUser.getCompany().getCompanyName();
			
			if(!companyName.equals("RTGS")){
				JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
						ResponseStatus.FOURZ30.getText(), ResponseStatus.FOURZ30.getValue());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
			}
			if (jwtUser != null && !basicToken.equals(jwtUser.getBasic_token())) {
				JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
						ResponseStatus.FOURZ6.getText(), ResponseStatus.FOURZ6.getValue());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
			}
		} else {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
					ResponseStatus.FOURZ0.getText(), ResponseStatus.FOURZ0.getValue());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		}

		// validate grant_type
		if (!grant_type.equals("password")) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
					ResponseStatus.FOURZ0.getText(), ResponseStatus.FOURZ0.getValue());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		}

		// authenticate(userName, password);
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

		final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(userName);
		String existingToken = tokenCache.get(userName);
		String token = "";

		try {
			if (existingToken != null && !jwtTokenUtil.isTokenExpired(existingToken)) {

				token = existingToken;
			} else {
				token = jwtTokenUtil.generateToken(userDetails);
				tokenCache.put(userName, token);
				apiLogService.save(apiLog);
			}
		} catch (ExpiredJwtException e) {
			token = jwtTokenUtil.generateToken(userDetails);
			tokenCache.put(userName, token);
			apiLogService.save(apiLog);
		}

		JwtResponse jwtResponse = new JwtResponse(token, JwtTokenUtil.JWT_TOKEN_VALIDITY, token, userName, userName,
				jwtTokenUtil.getIssuedAtDateFromToken(token), jwtTokenUtil.getExpirationDateFromToken(token));

		return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
	}

	@GetMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());

		String userName = jwtTokenUtil.getUsernameFromToken(token);

		JwtResponse jwtResponse = new JwtResponse(token, JwtTokenUtil.JWT_TOKEN_VALIDITY, token, userName, userName,
				jwtTokenUtil.getIssuedAtDateFromToken(token), jwtTokenUtil.getExpirationDateFromToken(token));

		return ResponseEntity.ok(jwtResponse);
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			// System.out.println(entry.getKey() + " " + entry.getValue());
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}

	private boolean checkRequestIP(String username, String remoteAddr) {
		try {
			JwtUser jwtUser = jwtUserService.findByUserName(username);
			String allowIP = jwtUser.getWhitelistIp().trim();
			String[] spiltData = allowIP.split(",");

			String[] splitRemoteAddrs = remoteAddr.split(",");

			for (String remoteIpValue : splitRemoteAddrs) {
				for (String ipValue : spiltData) {
					remoteIpValue = remoteIpValue.trim();
					if (remoteIpValue.equals(ipValue)) {
						return true;
					}
				}
			}

			return false;
		} catch (Exception e) {

		}
		return false;
	}

}
