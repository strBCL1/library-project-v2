package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.publisher.dto.PublisherIdDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
@JsonPropertyOrder({"isbnId", "title", "publisher"})
public class BookIdDto extends BookDataDto {

    @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!")
    private final String isbnId;

    public BookIdDto(String title, PublisherIdDto publisher, String isbnId) {
        super(title, publisher);
        this.isbnId = isbnId;
    }
}