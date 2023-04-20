package ru.practicum.shareit.util;

import ru.practicum.shareit.dto.BookingRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;


public class CheckDateValidator implements ConstraintValidator<StartBeforeEndDateValid, BookingRequestDto> {
    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(BookingRequestDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}
