package com.example.libraryprojectv2.domain.publisher.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PublisherIdDto {

    //    org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot construct instance of ...
    private final static Long DEFAULT_ID = 0L;

    @NotNull
    private final Long id;

    public PublisherIdDto(Long id) {
        this.id = id;
    }

    protected PublisherIdDto() {
        this.id = DEFAULT_ID;
    }
}
