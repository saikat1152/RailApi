package com.jbl.t24.rest.api.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import com.jbl.t24.rest.api.annotation.PasswordValueMatch;


public class PasswordFieldsValueMatchValidator implements ConstraintValidator<PasswordValueMatch, Object> {

    private String field;
    private String fieldMatch;
    private String message;

    @Override
	public void initialize(PasswordValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
        this.message = constraintAnnotation.message();
    }

    @Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

        String fieldValue = (String) new BeanWrapperImpl(value)
                .getPropertyValue(field);
        String fieldMatchValue = (String) new BeanWrapperImpl(value)
                .getPropertyValue(fieldMatch);

        boolean isValid = false;
        if (fieldValue != null) {
            isValid = fieldValue.equals(fieldMatchValue);
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(field)
                    .addConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fieldMatch)
                    .addConstraintViolation();
        }
        return isValid;
    }

}