package com.jbl.t24.rest.api.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;



import com.jbl.t24.rest.api.annotation.BankIdUnique;
import com.jbl.t24.rest.api.model.base.UserAccount;
import com.jbl.t24.rest.api.service.base.UserAccountService;

public class BankIdUniqueConstraintValidator implements ConstraintValidator<BankIdUnique, String> {

	// private boolean isCreate;
	@Autowired
	private UserAccountService useraccountService;

	@Override
	public void initialize(BankIdUnique bankId) {
		// isCreate = bankId.isCreate();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		boolean result = true;

			if (value != null) {
				try {
					UserAccount user = useraccountService.findOneByBankId(value);
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
