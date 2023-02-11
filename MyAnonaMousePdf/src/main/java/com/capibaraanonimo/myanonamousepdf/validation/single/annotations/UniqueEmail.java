package com.capibaraanonimo.myanonamousepdf.validation.single.annotations;


import com.capibaraanonimo.myanonamousepdf.validation.single.validators.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {

    String message() default "El email ya está en uso";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
