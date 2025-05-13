package com.jbl.t24.rest.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.jbl.t24.rest.api.utils.EmailUniqueConstraintValidator;


@Constraint(validatedBy = EmailUniqueConstraintValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailUnique {

	// define value
	// public String value();

	// define default message
	public String message() default "This email address exists!";

	// define default groups
	public Class<?>[] groups() default {};

	// define default payloads
	public Class<? extends Payload>[] payload() default {};

}
