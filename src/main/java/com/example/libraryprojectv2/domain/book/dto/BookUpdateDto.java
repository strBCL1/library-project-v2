package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.publisher.dto.PublisherOnlyIdDto;

import javax.validation.Valid;
import javax.validation.constraints.Size;

public record BookUpdateDto(
        @Size(max = 255, message = "Book's title may only contain up to 255 characters!") String title,
        @Valid PublisherOnlyIdDto publisher
) {}
