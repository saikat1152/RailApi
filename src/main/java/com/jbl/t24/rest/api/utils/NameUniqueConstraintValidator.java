package com.jbl.t24.rest.api.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.jbl.t24.rest.api.annotation.NameUnique;
import com.jbl.t24.rest.api.model.base.JwtUser;
import com.jbl.t24.rest.api.service.base.JwtUserService;


public class NameUniqueConstraintValidator implements ConstraintValidator<NameUnique, String> {

	// private String emailAddress;

	@Autowired
	private JwtUserService jwtUserService;

	@Override
	public void initialize(NameUnique valueObject) {
		// emailAddress = theEmail.value();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		boolean result = true;

		if (value != null) {
			try {
				JwtUser user = jwtUserService.findByUserName(value);
				if (user == null) {
					result = true;
				} else {
					result = false;
				}
			} catch (Exception exception) {

			}
		}
		return result;
	}

}
