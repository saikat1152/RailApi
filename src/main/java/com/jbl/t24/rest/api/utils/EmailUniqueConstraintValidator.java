package com.jbl.t24.rest.api.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.jbl.t24.rest.api.annotation.*;
import com.jbl.t24.rest.api.model.base.UserAccount;
import com.jbl.t24.rest.api.service.base.UserAccountService;



public class EmailUniqueConstraintValidator implements ConstraintValidator<EmailUnique, String> {

	// private String emailAddress;

	@Autowired
	private UserAccountService useraccountService;

	@Override
	public void initialize(EmailUnique theEmail) {
		// emailAddress = theEmail.value();
	}
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean result = true;
		if (value != null) {
			try {
				UserAccount user = useraccountService.findOneByEmail(value);
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
