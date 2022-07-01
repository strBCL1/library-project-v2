package com.example.libraryprojectv2.configuration.response.handler;

import com.example.libraryprojectv2.configuration.response.cause.Cause;
import com.example.libraryprojectv2.configuration.response.message.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;

import static java.text.MessageFormat.format;

@RestControllerAdvice
public class RestExceptionHandler {

    private final Cause cause;

    public RestExceptionHandler(Cause cause) {
        this.cause = cause;
    }

    @ExceptionHandler(value = { EntityNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleEntityNotFoundException(EntityNotFoundException exception) {
        return new ErrorMessage(exception.getMessage());
    }


    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleConstraintViolationException(ConstraintViolationException exception) {
        final String cause = exception.getMessage();
        return new ErrorMessage(cause.substring(cause.indexOf(": ") + 2, cause.indexOf("!") + 1));

//        Returns user's rejected values
//        final List<String> invalidValues = exception
//                .getConstraintViolations()
//                .stream()
//                .map(constraintViolation -> constraintViolation.getInvalidValue().toString())
//                .toList();
//
//        final String cause = exception.getMessage();
//        final String message = cause.substring(cause.indexOf(": ") + 2, cause.indexOf("!") + 1);
//
//        return new ErrorMessage(
//                format("''{0}'', rejected value(s): ''{1}''", message, invalidValues)
//        );
    }


    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final String message = exception.getFieldError().getDefaultMessage();
        final String rejectedValue = exception.getFieldError().getRejectedValue().toString();
        return new ErrorMessage(
                format("{0}, rejected value: ''{1}''", message, rejectedValue)
        );
    }


    @ExceptionHandler(value = { EntityExistsException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleEntityExistsException(EntityExistsException exception) {
        return new ErrorMessage(exception.getMessage());
    }


    @ExceptionHandler(value = { HttpMessageNotReadableException.class, UnexpectedTypeException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleHttpMessageNotReadableException(RuntimeException exception) {
        final String reason = exception.getMessage();
        final String errorMessage;

        if (reason.matches("(?s).*\\bbody\\b.*\\bis\\b.*\\bmissing\\b.*") || reason.matches("(?s).*\\bno\\b.*\\bcontent\\b.*\\bto\\b.*\\bmap\\b.*")) {
            errorMessage = cause.noDataSpecified();
        }
        else if (reason.matches("(?s).*\\bMissing\\b.*\\bcreator\\b.*\\bproperty\\b.*")) {
            final int nullPropertyBeginIndex = reason.indexOf("\'");
            final int nullPropertyEndIndex = reason.indexOf("\'", nullPropertyBeginIndex + 1) + 1;
            final String nullPropertyName = reason.substring(nullPropertyBeginIndex, nullPropertyEndIndex);
            errorMessage = nullPropertyName + cause.fieldIsNotSpecified();
        }
        else if (reason.matches("(?s).*\\bCannot\\b.*\\bdeserialize\\b.*\\bvalue\\b.*\\bfrom\\b.*\\bArray\\b.*")) {
            errorMessage = cause.specifiedArrayMustNotBeEmpty();
        }
        else {
            errorMessage = cause.jsonSyntaxError();
        }

        return new ErrorMessage(errorMessage);
    }
}
