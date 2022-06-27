package com.example.libraryprojectv2.domain.author.dto;

import com.example.libraryprojectv2.domain.book.dto.BookIdDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
@JsonPropertyOrder({"orcidId", "firstName", "lastName", "books"})
public class AuthorDto extends AuthorIdDto {

    @Valid
    private final Set<BookIdDto> books;

    public AuthorDto(String firstName, String lastName, String orcidId, Set<BookIdDto> books) {
        super(firstName, lastName, orcidId);
        this.books = books;
    }
}
