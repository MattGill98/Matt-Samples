package uk.me.mattgill.samples.bean.validation.constraints.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.me.mattgill.samples.bean.validation.constraints.Alphanumeric;

public class AlphanumericValidator implements ConstraintValidator<Alphanumeric, String> {

    @Override
    public void initialize(Alphanumeric constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("[A-Za-z0-9]+");
    }

}