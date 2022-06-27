package com.example.libraryprojectv2.domain.book.service;

import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.book.dto.BookDto;
import com.example.libraryprojectv2.domain.book.dto.BookInitDto;
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

        final List<BookDto> bookDtos = books
                .stream()
                .map(mapper::bookToBookDto)
                .toList();

        return bookDtos;
    }


    public BookDto getBookByIsbnId(final String isbnId) {
        final Book book = getBookByIsbnIdOrThrowEntityNotFoundException(isbnId);
        final BookDto bookDto = mapper.bookToBookDto(book);

        return bookDto;
    }


    @Transactional
    public BookDataDto createBook(final BookInitDto bookInitDto) {
        final Book newBook = mapper.bookInitDtoToBook(bookInitDto);
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
    public BookDto updateBookData(final BookInitDto bookInitDto, final String isbnId) {
        final Long publisherId = bookInitDto.publisher().id();
        final Publisher publisher = publisherRepository
                .findById(publisherId)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Publisher with ID of {0} not found!", publisherId)
                ));

        final Book book = getBookByIsbnIdOrThrowEntityNotFoundException(isbnId);
        book.updateBookData(bookInitDto.title(), publisher);

        final Book savedBook = bookRepository.save(book);
        final BookDto updatedBookDto = mapper.bookToBookDto(savedBook);
        return updatedBookDto;
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
