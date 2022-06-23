package com.example.libraryprojectv2.domain.book.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class BookDataDto extends BookIdDto {

    @Length(max = 255, message = "Book's title must only have from 1 to 255 characters!")
    private final String title;

    public BookDataDto(String isbnId, String title) {
        super(isbnId);
        this.title = title;
    }
}
