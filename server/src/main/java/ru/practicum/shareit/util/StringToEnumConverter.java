package ru.practicum.shareit.util;

import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.exception.ValidationException;


public class StringToEnumConverter implements Converter<String, RequestState> {
    @Override
    public RequestState convert(String source) {
        try {
            return RequestState.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + source);
        }
    }
}
