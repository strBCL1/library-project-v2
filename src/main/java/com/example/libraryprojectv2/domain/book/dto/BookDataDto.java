package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.publisher.dto.PublisherIdDto;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Getter
public class BookDataDto {

    @Size(max = 255, message = "Book's title may only contain up to 255 characters!")
    private final String title;

    @Valid
    private final PublisherIdDto publisher;

    public BookDataDto(String title, PublisherIdDto publisher) {
        this.title = title;
        this.publisher = publisher;
    }
}
