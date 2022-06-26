package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
public class BookDto extends BookDataDto {

    @Valid
    private final Set<AuthorIdDto> authors;

    public BookDto(String isbnId, String title, Set<AuthorIdDto> authors) {
        super(isbnId, title);
        this.authors = authors;
    }
}
