package com.example.libraryprojectv2.domain.book.dto;

import com.example.libraryprojectv2.domain.publisher.dto.PublisherOnlyIdDto;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;

public record BookInitDto(
        @Length(max = 255, message = "Book's title may only contain up to 255 characters!") String title,
        @Valid PublisherOnlyIdDto publisher
) {}
