package com.example.libraryprojectv2.domain.book.dto;

import javax.validation.Valid;
import java.util.List;

public record BookDataWithIsbnDtoList(@Valid List<BookDataWithIsbnDto> bookDataDtos) {}
