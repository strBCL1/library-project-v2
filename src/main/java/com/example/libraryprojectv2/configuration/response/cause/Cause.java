package com.example.libraryprojectv2.configuration.response.cause;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:causes.properties")
public record Cause(String noDataSpecified, String fieldIsNotSpecified, String specifiedArrayMustNotBeEmpty, String jsonSyntaxError) {

    public Cause(@Value("${message.noDataSpecified}") String noDataSpecified, @Value("${message.fieldIsNotSpecified}") String fieldIsNotSpecified,
                 @Value("${message.specifiedArrayMustNotBeEmpty}") String specifiedArrayMustNotBeEmpty, @Value("${message.jsonSyntaxError}") String jsonSyntaxError) {
        this.noDataSpecified = noDataSpecified;
        this.fieldIsNotSpecified = fieldIsNotSpecified;
        this.specifiedArrayMustNotBeEmpty = specifiedArrayMustNotBeEmpty;
        this.jsonSyntaxError = jsonSyntaxError;
    }

}
