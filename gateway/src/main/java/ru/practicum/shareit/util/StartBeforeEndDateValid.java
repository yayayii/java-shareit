package ru.practicum.shareit.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckDateValidator.class)
public @interface StartBeforeEndDateValid {
    String message() default "Start date should be before end date or start/end date should be not null.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
