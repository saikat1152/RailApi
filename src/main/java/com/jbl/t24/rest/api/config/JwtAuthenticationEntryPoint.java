package com.jbl.t24.rest.api.config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbl.t24.rest.api.enums.ResponseStatus;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		int responsecode = ResponseStatus.FIVEZ0.getValue();
		String message = ResponseStatus.FIVEZ0.getText();

		// System.out.println(authException);

		if (authException instanceof BadCredentialsException) {
			responsecode = ResponseStatus.FOURZ1.getValue();
			message = ResponseStatus.FOURZ1.getText();
		}

		if (authException instanceof InsufficientAuthenticationException) {
			responsecode = ResponseStatus.FIVEZ30.getValue();
			message = ResponseStatus.FIVEZ30.getText();
		}

		if (authException instanceof DisabledException) {
			responsecode = ResponseStatus.FOURZ19.getValue();
			message = ResponseStatus.FOURZ19.getText();
		}

		// CustomResponse customResponse = new CustomResponse();
		// Gson gson = new Gson();

		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, null, null);
		// JwtErrorResponse =

		// response.setContentType("application/json;charset=UTF-8");
		// response.setStatus(100);
		// response.getWriter().write(gson.toJson(customResponse));

		response.setContentType("application/json");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		Map<String, Object> data = new HashMap<>();
		data.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
		data.put("message", message);
		data.put("responseCode", responsecode);
		data.put("timestamp", new SimpleDateFormat("E, dd MM Y HH:mm:ss z").format(new Date()));

		OutputStream out = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, data);
		out.flush();

		/*
		 * response.setContentType("application/json"); String json =
		 * "{\"responsecode\": 8, \"message\": \"Method does not exist!\"}"; PrintWriter
		 * out = response.getWriter(); out.write(json);
		 */

		// response.getWriter().append("Not authenticated");
		// response.setStatus(401);

		// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}
