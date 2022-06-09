package com.example.libraryprojectv2.domain.book.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public record BookTitleDto(@NotBlank @Length(max = 255) String title) {}
