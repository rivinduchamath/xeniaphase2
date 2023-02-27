package com.cloudofgoods.xenia.config.validator;

import com.cloudofgoods.xenia.config.customAnnotations.NonNegative;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NonNegativeValidator implements ConstraintValidator<NonNegative, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null || value >= 0;
    }
}