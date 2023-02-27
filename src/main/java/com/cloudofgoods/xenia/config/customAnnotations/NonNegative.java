package com.cloudofgoods.xenia.config.customAnnotations;

import com.cloudofgoods.xenia.config.validator.NonNegativeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NonNegativeValidator.class)
public @interface NonNegative {
    String message() default "Value must be non-negative";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}