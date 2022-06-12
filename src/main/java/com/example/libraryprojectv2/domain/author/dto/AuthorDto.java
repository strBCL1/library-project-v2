package com.example.libraryprojectv2.domain.author.dto;

import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
public class AuthorDto extends AuthorDataDto {

    @Valid
    private final Set<BookDataDto> bookDataDtos;

    public AuthorDto(String orcidId, String firstName, String lastName, Set<BookDataDto> bookDataDtos) {
        super(orcidId, firstName, lastName);
        this.bookDataDtos = bookDataDtos;
    }
}
