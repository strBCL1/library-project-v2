package com.example.libraryprojectv2.domain.book.controller;

import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.book.dto.BookDataWithIsbnDto;
import com.example.libraryprojectv2.domain.book.dto.BookDto;
import com.example.libraryprojectv2.domain.book.dto.BookDtoList;
import com.example.libraryprojectv2.domain.book.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    public BookDtoList getAllBooks() {
        final BookDtoList bookDtoList = bookService.getAllBooks();
        return bookDtoList;
    }


    @GetMapping("/{isbn-id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getBookByIsbnId(@PathVariable("isbn-id") @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") final String isbnId) {
        final BookDto bookDto = bookService.getBookByIsbnId(isbnId);
        return bookDto;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDataWithIsbnDto createBook(@RequestBody @NotNull @Valid final BookDataWithIsbnDto bookDataWithIsbnDto) {
        final BookDataWithIsbnDto createdBook = bookService.createBook(bookDataWithIsbnDto);
        return createdBook;
    }

    @PutMapping("/{isbn-id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDataDto updateBookData(@RequestBody @NotNull @Valid final BookDataDto bookDataDto,
                                      @PathVariable("isbn-id") @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") final String isbnId) {
        final BookDataDto updatedBookDataDto = bookService.updateBookData(bookDataDto, isbnId);
        return updatedBookDataDto;
    }

    @DeleteMapping("/{isbn-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable("isbn-id") @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") final String isbnId) {
        bookService.deleteBook(isbnId);
    }
}
