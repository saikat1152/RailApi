package com.jbl.t24.rest.api.custom.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = TxCategoryValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTxCategory {

    String message() default "Invalid Transaction Category Provided!";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{}; 
    
}
