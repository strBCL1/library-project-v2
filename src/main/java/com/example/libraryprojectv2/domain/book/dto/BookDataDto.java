package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.publisher.dto.PublisherIdDto;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;

@Getter
public class BookDataDto {

    @Length(max = 255, message = "Book's title may only contain up to 255 characters!")
    private final String title;

    @Valid
    private final PublisherIdDto publisher;

    public BookDataDto(String title, PublisherIdDto publisher) {
        this.title = title;
        this.publisher = publisher;
    }
}
