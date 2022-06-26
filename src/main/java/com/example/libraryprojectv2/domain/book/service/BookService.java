package com.example.libraryprojectv2.domain.book.service;

import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.book.dto.BookDto;
import com.example.libraryprojectv2.domain.book.dto.BookTitleDto;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.mapper.Mapper;
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
    private final Mapper mapper;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, Mapper mapper, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.authorRepository = authorRepository;
    }

    public List<BookDto> getAllBooks() {
        final List<Book> books = bookRepository.findAll();

        final List<BookDto> bookDtos = books
                .stream()
                .map(book -> mapper.bookToBookDto(book))
                .toList();

        return bookDtos;
    }


    public BookDto getBookByIsbnId(final String isbnId) {
        final Book book = getBookByIsbnIdOrThrowEntityNotFoundException(isbnId);
        final BookDto bookDto = mapper.bookToBookDto(book);

        return bookDto;
    }


    @Transactional
    public BookDataDto createBook(final BookDataDto bookDataDto) {
        final Book newBook = mapper.bookDataDtoToBook(bookDataDto);
        final String newBookIsbnId = newBook.getIsbnId();

        final Optional<Book> bookOptional = bookRepository.findById(newBookIsbnId);

        if (bookOptional.isPresent()) {
            throw new EntityExistsException(
                    format("Book with ISBN code of {0} already exists!", newBookIsbnId)
            );
        }

        final Book savedBook = bookRepository.save(newBook);
        final BookDataDto savedBookDataDto = mapper.bookToBookDataDto(savedBook);
        return savedBookDataDto;
    }


    @Transactional
    public BookTitleDto updateBookData(final BookTitleDto bookTitleDto, final String isbnId) {
        final Book book = getBookByIsbnIdOrThrowEntityNotFoundException(isbnId);

        book.updateBookData(bookTitleDto.title());

        final Book savedBook = bookRepository.save(book);
        final BookTitleDto updatedBookTitleDto = mapper.bookToBookTitleDto(savedBook);
        return updatedBookTitleDto;
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
