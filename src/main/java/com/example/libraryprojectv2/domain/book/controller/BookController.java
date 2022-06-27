package com.example.libraryprojectv2.domain.book.controller;

import com.example.libraryprojectv2.domain.book.dto.*;
import com.example.libraryprojectv2.domain.book.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/books")
@Validated
public class BookController {
    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }


    @GetMapping("/{isbn-id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getBookByIsbnId(@PathVariable("isbn-id") @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") final String isbnId) {
        return bookService.getBookByIsbnId(isbnId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookIdDto createBook(@RequestBody @NotNull @Valid final BookInitDto bookInitDto) {
        return bookService.createBook(bookInitDto);
    }


    @PutMapping("/{isbn-id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto updateBookData(@RequestBody @NotNull @Valid final BookUpdateDto bookUpdateDto,
                                       @PathVariable("isbn-id") @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") final String isbnId) {
        return bookService.updateBookData(bookUpdateDto, isbnId);
    }


    @DeleteMapping("/{isbn-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable("isbn-id") @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") final String isbnId) {
        bookService.deleteBook(isbnId);
    }
}
