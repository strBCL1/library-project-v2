package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.publisher.dto.PublisherOnlyIdDto;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record BookInitDto(
        @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") String isbnId,
        @Size(max = 255, message = "Book's title may only contain up to 255 characters!") String title,
        @Valid PublisherOnlyIdDto publisher
) {}
