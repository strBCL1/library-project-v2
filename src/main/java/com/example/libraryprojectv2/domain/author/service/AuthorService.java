package com.example.libraryprojectv2.domain.author.service;

import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorDtoList;
import com.example.libraryprojectv2.domain.author.dto.AuthorWithOrcidDto;
import com.example.libraryprojectv2.domain.author.mapper.AuthorMapper;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.BookIsbnDtoList;
import com.example.libraryprojectv2.domain.book.mapper.BookMapper;
import com.example.libraryprojectv2.domain.book.model.Book;
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
    private final AuthorMapper authorMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper, BookRepository bookRepository, BookMapper bookMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }


    @Transactional
    public AuthorDataDto createAuthor(final AuthorWithOrcidDto authorWithOrcidDto) {
        final Author newAuthor = authorMapper.authorOrcidDtoToAuthor(authorWithOrcidDto);
        final String newAuthorOrcidId = newAuthor.getOrcidId();

        final Optional<Author> optionalAuthor = authorRepository.findById(newAuthorOrcidId);

        if (optionalAuthor.isPresent()) {
            throw new EntityExistsException(
                    format("Author with ORCID code of {0} already exists!", newAuthorOrcidId)
            );
        }

        final Author savedAuthor = authorRepository.save(newAuthor);
        final AuthorDataDto savedAuthorDataDto = authorMapper.authorToAuthorOrcidDto(savedAuthor);
        return savedAuthorDataDto;
    }


    public AuthorDto getAuthorByOrcidId(final String orcidId) {
        final Author author = getAuthorOrThrowEntityNotFoundException(orcidId);

        final AuthorDto authorDto = authorMapper.authorToAuthorDto(author);
        return authorDto;
    }

    @Transactional
    public AuthorDto updateBooksOfAuthor(final BookIsbnDtoList bookIsbnDtoList, final String orcidId) {
        final Author author = getAuthorOrThrowEntityNotFoundException(orcidId);

        final Set<Book> authorBooks = author.getBooks();
        final Set<Book> newBooks = bookIsbnDtoList
                .bookIsbnDtos()
                .stream()
                .map(bookMapper::bookIsbnDtoToBook)
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
        final AuthorDto authorDto = authorMapper.authorToAuthorDto(updatedAuthor);
        return authorDto;
    }


    public AuthorDtoList getAuthors() {
        final List<Author> authors = authorRepository.findAll();

        final List<AuthorDto> authorDtos = authors
                .stream()
                .map(authorMapper::authorToAuthorDto)
                .toList();

        final AuthorDtoList authorDtoList = new AuthorDtoList(authorDtos);

        return authorDtoList;
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
    public AuthorDataDto updateAuthorData(final AuthorDataDto authorDataDto, final String orcidId) {
        final Author author = getAuthorOrThrowEntityNotFoundException(orcidId);

        author.updateAuthorData(authorDataDto.getFirstName(), authorDataDto.getLastName());

        final Author savedAuthor = authorRepository.save(author);
        final AuthorDataDto savedAuthorDataDto = authorMapper.authorToAuthorOrcidDto(savedAuthor);
        return savedAuthorDataDto;
    }


    private Author getAuthorOrThrowEntityNotFoundException(final String orcidId) {
        return authorRepository
                .findById(orcidId)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Author with ORCID code of {0} not found!", orcidId)
                ));
    }
}
