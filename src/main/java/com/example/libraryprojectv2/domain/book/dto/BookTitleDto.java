package com.example.libraryprojectv2.domain.book.dto;

import org.hibernate.validator.constraints.Length;

public record BookTitleDto(@Length(max = 255, message = "Book's title must only have from 1 to 255 characters!") String title) {}
