package com.example.libraryprojectv2.domain.book.service;

import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.book.dto.BookDataWithIsbnDto;
import com.example.libraryprojectv2.domain.book.dto.BookDto;
import com.example.libraryprojectv2.domain.book.dto.BookDtoList;
import com.example.libraryprojectv2.domain.book.mapper.BookMapper;
import com.example.libraryprojectv2.domain.book.model.Book;
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
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorRepository = authorRepository;
    }

    public BookDtoList getAllBooks() {
        final List<Book> books = bookRepository.findAll();

        final List<BookDto> bookDtos = books
                .stream()
                .map(bookMapper::bookToBookDto)
                .toList();

        final BookDtoList bookDtoList = new BookDtoList(bookDtos);

        return bookDtoList;
    }


    public BookDto getBookByIsbnId(final String isbnId) {
        final Book book = getBookByIsbnIdOrThrowEntityNotFoundException(isbnId);
        final BookDto bookDto = bookMapper.bookToBookDto(book);

        return bookDto;
    }


    @Transactional
    public BookDataWithIsbnDto createBook(final BookDataWithIsbnDto bookDataWithIsbnDto) {
        final Book newBook = bookMapper.bookDataWithIsbnDtoToBook(bookDataWithIsbnDto);
        final String newBookIsbnId = newBook.getIsbnId();

        final Optional<Book> bookOptional = bookRepository.findById(newBookIsbnId);

        if (bookOptional.isPresent()) {
            throw new EntityExistsException(
                    format("Book with ISBN code of {0} already exists!", newBookIsbnId)
            );
        }

        final Book savedBook = bookRepository.save(newBook);
        final BookDataWithIsbnDto savedBookDataWithIsbnDto = bookMapper.bookToBookDataWithIsbnDto(savedBook);
        return savedBookDataWithIsbnDto;
    }


    @Transactional
    public BookDataDto updateBookData(final BookDataDto bookDataDto, final String isbnId) {
        final Book book = getBookByIsbnIdOrThrowEntityNotFoundException(isbnId);

        book.updateBookData(bookDataDto.getTitle());

        final Book savedBook = bookRepository.save(book);
        final BookDataDto savedBookDataDto = bookMapper.bookToBookDataDto(savedBook);
        return savedBookDataDto;
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
