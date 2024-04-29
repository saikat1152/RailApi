package com.jbl.t24.rest.api.custom.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;

public class TxCategoryValidator implements ConstraintValidator<ValidTxCategory, String> {

    @Override
    public void initialize(ValidTxCategory constraintAnnotation) {
        // No initialization needed for this validator
    }

    @Override
    public boolean isValid(String category, ConstraintValidatorContext context) {
        return RtgsTransactionConstants.rtgsConstants.containsKey(category);
    }
    
}
