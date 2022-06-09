package com.example.libraryprojectv2.domain.author.dto;

import com.example.libraryprojectv2.domain.book.dto.BookTitleDto;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public record AuthorDto(
        @NotBlank @Length(max = 45) String firstName,
        @NotBlank @Length(max = 45) String lastName,
        @Valid Set<BookTitleDto> bookTitleDtos) {}
