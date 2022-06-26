package com.example.libraryprojectv2.domain.book.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class BookIdDto {

    //    org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot construct instance of ...
    private static final String DEFAULT_ISBN_ID = "0000000000000";

    @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!")
    private final String isbnId;

    public BookIdDto(String isbnId) {
        this.isbnId = isbnId;
    }

    protected BookIdDto() {
        this.isbnId = DEFAULT_ISBN_ID;
    }
}