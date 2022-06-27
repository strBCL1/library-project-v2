package com.example.libraryprojectv2.domain.book.dto;

import javax.validation.constraints.Pattern;

public record BookIsbnIdDto(@Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") String isbnId) {}