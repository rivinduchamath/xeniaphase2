package com.cloudofgoods.xenia.config.customAnnotations.validator;

import com.cloudofgoods.xenia.config.customAnnotations.NonZero;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NonZeroValidator implements ConstraintValidator<NonZero, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null || value == 0 || value >= 0;
    }
}
