package ru.ingredients.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinLessThanMaxValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinLessThanMax {
    String message() default "Минимальное значение не может быть больше максимального";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
