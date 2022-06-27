package com.example.libraryprojectv2.domain.author.service;

import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.BookIsbnIdDto;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final Mapper mapper;


    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository, Mapper mapper) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }


    @Transactional
    public AuthorIdDto createAuthor(final AuthorIdDto authorIdDto) {
        final Author newAuthor = mapper.authorIdDtoToAuthor(authorIdDto);
        final String newAuthorOrcidId = newAuthor.getOrcidId();

        final Optional<Author> optionalAuthor = authorRepository.findById(newAuthorOrcidId);

        if (optionalAuthor.isPresent()) {
            throw new EntityExistsException(
                    format("Author with ORCID code of {0} already exists!", newAuthorOrcidId)
            );
        }

        final Author savedAuthor = authorRepository.save(newAuthor);
        final AuthorIdDto savedAuthorIdDto = mapper.authorToAuthorIdDto(savedAuthor);
        return savedAuthorIdDto;
    }


    public AuthorDto getAuthorByOrcidId(final String orcidId) {
        final Author author = getAuthorByOrcidIdOrThrowEntityNotFoundException(orcidId);

        final AuthorDto authorDto = mapper.authorToAuthorDto(author);
        return authorDto;
    }


    @Transactional
    public AuthorDto updateBooks(final List<BookIsbnIdDto> bookIsbnDtos, final String orcidId) {
        final Author author = getAuthorByOrcidIdOrThrowEntityNotFoundException(orcidId);

        final Set<Book> authorBooks = author.getBooks();
        final Set<Book> newBooks = bookIsbnDtos
                .stream()
                .map(mapper::bookIsbnIdDtoToBook)
                .collect(Collectors.toSet());

//        Convert list of all books to set to minimize search time complexity
        final Set<Book> allBooks = new HashSet<>(bookRepository.findAll());

        newBooks.forEach(book -> {
            if (authorBooks.contains(book)) {
                return;
            }

            final Book existingBook = allBooks
                    .stream()
                    .filter(databaseBook -> databaseBook.equals(book))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(
                            format("Book with ISBN code of {0} not found!", book.getIsbnId())
                    ));

            author.addBook(existingBook);
        });

        final Author updatedAuthor = authorRepository.save(author);
        final AuthorDto authorDto = mapper.authorToAuthorDto(updatedAuthor);
        return authorDto;
    }


    public List<AuthorDto> getAuthors() {
        final List<Author> authors = authorRepository.findAll();

        final List<AuthorDto> authorDtos = authors
                .stream()
                .map(mapper::authorToAuthorDto)
                .toList();

        return authorDtos;
    }


    @Transactional
    public void deleteAuthor(final String orcidId) {
        if (!authorRepository.existsById(orcidId)) {
            throw new EntityNotFoundException(
                    format("Author with ORCID code of {0} not found!", orcidId)
            );
        }

        authorRepository.deleteById(orcidId);
    }


    @Transactional
    public AuthorDto updateAuthorData(final AuthorDataDto authorDataDto, final String orcidId) {
        final Author author = getAuthorByOrcidIdOrThrowEntityNotFoundException(orcidId);

        author.updateAuthorData(authorDataDto.getFirstName(), authorDataDto.getLastName());

        final Author savedAuthor = authorRepository.save(author);
        final AuthorDto savedAuthorDto = mapper.authorToAuthorDto(savedAuthor);
        return savedAuthorDto;
    }


    private Author getAuthorByOrcidIdOrThrowEntityNotFoundException(final String orcidId) {
        return authorRepository
                .findById(orcidId)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Author with ORCID code of {0} not found!", orcidId)
                ));
    }
}
