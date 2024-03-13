package ru.yandex.practicum.filmorate.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class CheckDateValidator implements ConstraintValidator<CheckDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintContext) {
        LocalDate dateMin = LocalDate.of(1895, 12, 28);
        return date == null || !date.isBefore(dateMin);
    }
}


