package com.example.libraryprojectv2.domain.book.service;

import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.BookDto;
import com.example.libraryprojectv2.domain.book.mapper.BookMapper;
import com.example.libraryprojectv2.domain.book.model.Book;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static java.text.MessageFormat.format;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public BookDto getBookById(final long id) {
        final Book book = bookRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Book with id of {0} not found!", id)
                ));

        final BookDto bookDto = bookMapper.bookToBookDto(book);
        return bookDto;
    }
}
