package ru.ingredients.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.ingredients.dto.PercentDTO;

public class MinLessThanMaxValidator implements ConstraintValidator<MinLessThanMax, PercentDTO> {

    @Override
    public boolean isValid(PercentDTO value, ConstraintValidatorContext context) {
        if (value.getMin() == 0 || value.getMax() == 0) {
            return true;
        }

        if (value.getMin() > value.getMax()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Минимум не может быть больше максимума")
                    .addPropertyNode("min")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
