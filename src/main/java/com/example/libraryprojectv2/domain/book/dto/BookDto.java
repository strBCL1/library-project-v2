package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.author.dto.AuthorNameDto;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public record BookDto(
        @NotBlank @Length(max = 255) String title,

        @Valid Set<AuthorNameDto> authorNameDtos) {}

