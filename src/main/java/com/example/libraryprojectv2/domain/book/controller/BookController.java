package com.example.libraryprojectv2.domain.book.controller;

import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.book.dto.BookDto;
import com.example.libraryprojectv2.domain.book.dto.BookInitDto;
import com.example.libraryprojectv2.domain.book.dto.BookTitleDto;
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
        final List<BookDto> bookDtos = bookService.getAllBooks();
        return bookDtos;
    }


    @GetMapping("/{isbn-id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getBookByIsbnId(@PathVariable("isbn-id") @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") final String isbnId) {
        final BookDto bookDto = bookService.getBookByIsbnId(isbnId);
        return bookDto;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDataDto createBook(@RequestBody @NotNull @Valid final BookInitDto bookInitDto) {
        final BookDataDto createdBook = bookService.createBook(bookInitDto);
        return createdBook;
    }


    @PutMapping("/{isbn-id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto updateBookData(@RequestBody @NotNull @Valid final BookInitDto bookInitDto,
                                       @PathVariable("isbn-id") @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") final String isbnId) {
        final BookDto updatedBookDto = bookService.updateBookData(bookInitDto, isbnId);
        return updatedBookDto;
    }


    @DeleteMapping("/{isbn-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable("isbn-id") @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!") final String isbnId) {
        bookService.deleteBook(isbnId);
    }
}
