package com.example.libraryprojectv2.domain.book.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public record BookTitleDto(@NotNull @Length(min = 1, max = 255, message = "Book's title must only have from 1 to 255 characters!") String title) {}
