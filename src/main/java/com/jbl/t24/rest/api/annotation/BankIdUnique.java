package com.jbl.t24.rest.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.jbl.t24.rest.api.utils.BankIdUniqueConstraintValidator;

@Constraint(validatedBy = BankIdUniqueConstraintValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BankIdUnique {


	// define value
	// public String value();

	// define default message
	public String message() default "This Bank ID is already exist!";

	// define default groups
	public Class<?>[] groups() default {};


	// define default payloads
	public Class<? extends Payload>[] payload() default {};

	// boolean isCreate() default true;

}
