package com.cloudofgoods.xenia.config.customAnnotations;

import com.cloudofgoods.xenia.config.validator.NotEmptyOrNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NotEmptyOrNullValidator.class)
public @interface NotEmptyOrNull {
    String message() default "Field must not be empty or null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}