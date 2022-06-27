package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Set;

public record BookFullInfoDto(
        @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") String isbnId,
        @Length(max = 255, message = "Book's title may only contain up to 255 characters!") String title,
        @Valid Set<AuthorIdDto> authors
) {}
