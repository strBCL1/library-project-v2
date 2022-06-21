package com.example.libraryprojectv2.domain.author.dto;

import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
public class AuthorDto extends AuthorIdDto {

    @Valid
    private final Set<BookDataDto> books;

    public AuthorDto(String firstName, String lastName, String orcidId, Set<BookDataDto> books) {
        super(firstName, lastName, orcidId);
        this.books = books;
    }
}
