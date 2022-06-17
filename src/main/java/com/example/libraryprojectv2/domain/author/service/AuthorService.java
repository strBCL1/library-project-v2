package com.example.libraryprojectv2.domain.author.service;

import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
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
    public AuthorDataDto createAuthor(final AuthorDataDto authorDataDto) {
        final Author newAuthor = authorMapper.authorDataDtoToAuthor(authorDataDto);
        final String newAuthorOrcidId = newAuthor.getOrcidId();

        final Optional<Author> optionalAuthor = authorRepository.findById(newAuthorOrcidId);

        if (optionalAuthor.isPresent()) {
            throw new EntityExistsException(
                    format("Author with ORCID code of {0} already exists!", newAuthorOrcidId)
            );
        }

        final Author savedAuthor = authorRepository.save(newAuthor);
        final AuthorDataDto savedAuthorDataDto = authorMapper.authorToAuthorDataDto(savedAuthor);
        return savedAuthorDataDto;
    }

    public AuthorDto getAuthorByOrcidId(final String orcidId) {
        final Author author = authorRepository
                .findById(orcidId)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Author with ORCID code of {0} not found!", orcidId)
                ));

        final AuthorDto authorDto = authorMapper.authorToAuthorDto(author);
        return authorDto;
    }

    @Transactional
    public AuthorDto addOrUpdateBooksOfAuthor(final BookIsbnDtoList bookIsbnDtoList, final String orcidId) {
        final Author author = authorRepository
                .findById(orcidId)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Author with ORCID code of {0} not found!", orcidId)
                ));

        final Set<Book> authorBooks = author.getBooks();
        final Set<Book> newBooks = bookIsbnDtoList
                .bookDataDtos()
                .stream()
                .map(bookMapper::BookIsbnDtoToBook)
                .collect(Collectors.toSet());

//        Convert list of all books to set to minimize search time complexity
        final Set<Book> allBooks = new HashSet<>(bookRepository.findAll());

        newBooks.forEach(book -> {
            if (authorBooks.contains(book)) {
                return;
            }

            if (!allBooks.contains(book)) {
                throw new EntityNotFoundException(
                        format("Book with ISBN code of {0} not found!", book.getIsbnId())
                );
            }

            author.addBook(book);
        });

        final Author updatedAuthor = authorRepository.save(author);
        final AuthorDto authorDto = authorMapper.authorToAuthorDto(updatedAuthor);
        return authorDto;
    }
}
