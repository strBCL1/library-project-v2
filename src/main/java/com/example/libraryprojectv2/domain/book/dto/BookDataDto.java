package com.example.libraryprojectv2.domain.book.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
public class BookDataDto extends BookIdDto {

    @NotNull @Length(min = 1, max = 255, message = "Book's title must only have from 1 to 255 characters!")
    private final String title;

    public BookDataDto(String isbnId, String title) {
        super(isbnId);
        this.title = title;
    }
}
