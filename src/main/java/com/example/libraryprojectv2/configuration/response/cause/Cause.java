package com.example.libraryprojectv2.configuration.response.cause;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:causes.properties")
@Getter
public class Cause {
    private final String noDataSpecified;
    private final String fieldIsNotInitialized;
    private final String isEmptyOrInvalidObject;
    private final String jsonSyntaxError;


    public Cause(@Value("${message.noDataSpecified}") String noDataSpecified, @Value("${message.fieldIsNotInitialized}") String fieldIsNotInitialized,
                 @Value("${message.IsEmptyOrInvalidObject}") String isEmptyOrInvalidObject, @Value("${message.jsonSyntaxError}") String jsonSyntaxError) {
        this.noDataSpecified = noDataSpecified;
        this.fieldIsNotInitialized = fieldIsNotInitialized;
        this.isEmptyOrInvalidObject = isEmptyOrInvalidObject;
        this.jsonSyntaxError = jsonSyntaxError;
    }
}
