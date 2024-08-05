package com.satvik.stockpdfspringboot.Authentication.util;


import jakarta.validation.Constraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.websocket.OnMessage;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Email(message = "Invalid email")
@Pattern(regexp = ".+@.+\\..+", message = "Invalid email")
@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
public @interface ExtendedEmailValidator {
    String message() default "Invalid email";
    Class<?> [] groups() default {};
    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
