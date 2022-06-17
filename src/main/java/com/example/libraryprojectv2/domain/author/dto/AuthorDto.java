package com.example.libraryprojectv2.domain.author.dto;

import com.example.libraryprojectv2.domain.book.dto.BookDataWithIsbnDto;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
public class AuthorDto extends AuthorWithOrcidDto {

    @Valid
    private final Set<BookDataWithIsbnDto> bookDataDtos;

    public AuthorDto(String firstName, String lastName, String orcidId, Set<BookDataWithIsbnDto> bookDataDtos) {
        super(firstName, lastName, orcidId);
        this.bookDataDtos = bookDataDtos;
    }
}
