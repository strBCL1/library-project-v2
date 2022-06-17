package com.example.libraryprojectv2.domain.book.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class BookDataWithIsbnDto extends BookDataDto {
    private static final String DEFAULT_ISBN_ID = "0000000000000";

    @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!")
    private final String isbnId;

    public BookDataWithIsbnDto(String title, String isbnId) {
        super(title);
        this.isbnId = isbnId;
    }
}