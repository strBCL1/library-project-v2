package com.example.libraryprojectv2.domain.author.dto;

import javax.validation.Valid;
import java.util.List;

public record AuthorDtoList(@Valid List<AuthorDto> authorDtos) {}
