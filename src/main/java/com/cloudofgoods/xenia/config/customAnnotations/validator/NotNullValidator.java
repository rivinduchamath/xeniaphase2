package com.cloudofgoods.xenia.config.customAnnotations.validator;

import com.cloudofgoods.xenia.config.customAnnotations.NotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullValidator implements ConstraintValidator<NotNull, Boolean> {

    @Override
    public boolean isValid(Boolean aBoolean, ConstraintValidatorContext constraintValidatorContext) {
        return aBoolean != null;
    }
}
