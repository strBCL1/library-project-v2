package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherIdDto;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
public class BookDto extends BookIdDto {

    @Valid
    private final Set<AuthorIdDto> authors;

    public BookDto(String title, PublisherIdDto publisher, String isbnId, Set<AuthorIdDto> authors) {
        super(title, publisher, isbnId);
        this.authors = authors;
    }
}
