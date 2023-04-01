package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleValidationException(final ValidationException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleMissingRequestHeaderException(final MissingRequestHeaderException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ErrorResponse handleForbiddenActionException(final ForbiddenActionException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleNoSuchElementException(final NoSuchElementException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ErrorResponse> handleConverterException(final MethodArgumentTypeMismatchException e) {
        Throwable cause = e.getCause().getCause();
        if (cause.getClass() == ValidationException.class) {
            return new ResponseEntity<>(new ErrorResponse(cause.getMessage()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Произошла непредвиденная ошибка: " + e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleException(final Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Произошла непредвиденная ошибка: " + e);
    }
}
