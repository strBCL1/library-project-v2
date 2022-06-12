package com.example.libraryprojectv2.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Getter
public class BookDataDto {

//    Book's id can only have max 13 digits
    @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!")
    private final String isbnId;

    @Length(min = 1, max = 255, message = "Book's title must only have from 1 to 255 characters!")
    private final String title;
}
