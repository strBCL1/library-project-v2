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
        final String message = exception.getMessage();

        final String errorMessage;

        if (message.matches("(?s).*\\bmust\\b.*\\bnot\\b.*\\bbe\\b.*\\bempty\\b.*")) {
            errorMessage = cause.getIsEmptyOrInvalidObject();
        }
        else {
            int messageBeginIndex = message.indexOf(": ") + 2;
            int messageEndIndex = message.indexOf("!");

            if (messageEndIndex == -1) {
                messageEndIndex = message.length();
            }

            errorMessage = message.substring(messageBeginIndex, messageEndIndex + 1);
        }

        return new ErrorMessage(errorMessage);

//        Returns user's rejected values
//        final List<String> invalidValues = exception
//                .getConstraintViolations()
//                .stream()
//                .map(constraintViolation -> constraintViolation.getInvalidValue().toString())
//                .toList();
//
//        final String message = exception.getMessage();
//        final String message = message.substring(message.indexOf(": ") + 2, message.indexOf("!") + 1);
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

        if (reason.matches("(?s).*\\bbody\\b.*\\bis\\b.*\\bmissing\\b.*") ||
                reason.matches("(?s).*\\bno\\b.*\\bcontent\\b.*\\bto\\b.*\\bmap\\b.*") ||
                reason.matches("(?s).*\\bCannot\\b.*\\bdeserialize\\b.*List.*") ||
                reason.matches("(?s).*\\bCannot\\b.*\\bdeserialize\\b.*Array.*")) {
            errorMessage = cause.getIsEmptyOrInvalidObject();
        }
        else if (reason.matches("(?s).*\\bMissing\\b.*\\bcreator\\b.*\\bproperty\\b.*")) {
            final int nullPropertyNameBeginIndex = reason.indexOf("\'");
            final int nullPropertyNameEndIndex = reason.indexOf("\'", nullPropertyNameBeginIndex + 1) + 1;
            final String nullPropertyName = reason.substring(nullPropertyNameBeginIndex, nullPropertyNameEndIndex);
            errorMessage = nullPropertyName + cause.getFieldIsNotInitialized();
        }
        else {
            errorMessage = cause.getJsonSyntaxError();
        }

        return new ErrorMessage(errorMessage);
    }
}
