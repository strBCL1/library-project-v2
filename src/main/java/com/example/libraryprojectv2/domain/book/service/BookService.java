package com.example.libraryprojectv2.domain.book.service;

import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.*;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import com.example.libraryprojectv2.domain.publisher.dao.PublisherRepository;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final Mapper mapper;


    public BookService(BookRepository bookRepository, PublisherRepository publisherRepository, AuthorRepository authorRepository, Mapper mapper) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }


    public List<BookDto> getAllBooks() {
        final List<Book> books = bookRepository.findAll();

        return books
                .stream()
                .map(mapper::bookToBookDto)
                .toList();
    }


    public BookDto getBookByIsbnId(final String isbnId) {
        final Book book = getBookByIsbnIdOrThrowEntityNotFoundException(isbnId);

        return mapper.bookToBookDto(book);
    }


    @Transactional
    public BookIdDto createBook(final BookInitDto bookInitDto) {
        final Book newBook = mapper.bookInitDtoToBook(bookInitDto);
        final String newBookIsbnId = newBook.getIsbnId();

        final Optional<Book> bookOptional = bookRepository.findById(newBookIsbnId);

        if (bookOptional.isPresent()) {
            throw new EntityExistsException(
                    format("Book with ISBN code of {0} already exists!", newBookIsbnId)
            );
        }

        final long publisherId = bookInitDto.publisher().id();
        final Optional<Publisher> optionalPublisher = publisherRepository.findById(publisherId);

        if (optionalPublisher.isEmpty()) {
            throw new EntityNotFoundException(
                    format("Publisher with ID of {0} not found!", publisherId)
            );
        }

        final Book savedBook = bookRepository.save(newBook);
        return mapper.bookToBookIdDto(savedBook);
    }


    @Transactional
    public BookDto updateBookData(final BookUpdateDto bookUpdateDto, final String isbnId) {
        final Long publisherId = bookUpdateDto.publisher().id();
        final Publisher publisher = publisherRepository
                .findById(publisherId)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Publisher with ID of {0} not found!", publisherId)
                ));

        final Book book = getBookByIsbnIdOrThrowEntityNotFoundException(isbnId);
        book.updateBookData(bookUpdateDto.title(), publisher);

        final Book savedBook = bookRepository.save(book);
        return mapper.bookToBookDto(savedBook);
    }


    @Transactional
    public void deleteBook(final String isbnId) {
        if (!bookRepository.existsById(isbnId)) {
            throw new EntityNotFoundException(
                    format("Book with ISBN code of {0} not found!", isbnId)
            );
        }

//        Find authors of book with given ISBN code
        final List<Author> authors = authorRepository.findByBooks_IsbnIdEquals(isbnId);

//        Remove book with given ISBN code from join table
        authors.forEach(author -> author.getBooks().removeIf(book -> book.getIsbnId().equals(isbnId)));

//        Remove book with given isbnId from 'book' table
        bookRepository.deleteById(isbnId);
    }


    private Book getBookByIsbnIdOrThrowEntityNotFoundException(final String isbnId) {
        return bookRepository
                .findById(isbnId)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Book with ISBN code of {0} not found!", isbnId)
                ));
    }
}
