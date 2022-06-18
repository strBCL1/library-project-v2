package com.example.libraryprojectv2.domain.book.controller;

import com.example.libraryprojectv2.domain.book.dto.BookDto;
import com.example.libraryprojectv2.domain.book.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/books")
@Validated
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/{isbn-id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getBookByIsbnId(@PathVariable("isbn-id") @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") final String isbnId) {
        final BookDto bookDto = bookService.getBookByIsbnId(isbnId);

        return bookDto;
    }
}
