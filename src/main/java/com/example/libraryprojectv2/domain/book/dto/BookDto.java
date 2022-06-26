package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
public class BookDto extends BookDataDto {

    @Valid
    private final Set<AuthorDataDto> authors;

    public BookDto(String isbnId, String title, Set<AuthorDataDto> authors) {
        super(isbnId, title);
        this.authors = authors;
    }
}
