package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.publisher.dto.PublisherOnlyIdDto;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

public record BookInitDto(
        @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") String isbnId,
        @Length(max = 255, message = "Book's title may only contain up to 255 characters!") String title,
        @Valid PublisherOnlyIdDto publisher
) {}
