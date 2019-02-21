package uk.me.mattgill.samples.bean.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import uk.me.mattgill.samples.bean.validation.constraints.validator.AlphanumericValidator;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlphanumericValidator.class)
public @interface Alphanumeric {

    String message() default "String isn't alphanumeric";

    // Required by validation runtime
    Class<?>[] groups() default {};

    // Required by validation runtime
    Class<? extends Payload>[] payload() default {};

}