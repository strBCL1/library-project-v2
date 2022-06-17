package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
public class BookDto extends BookDataWithIsbnDto {

    @Valid
    private final Set<AuthorDataDto> authorDataDtos;

    public BookDto(String title, String isbnId, Set<AuthorDataDto> authorDataDtos) {
        super(title, isbnId);
        this.authorDataDtos = authorDataDtos;
    }
}
