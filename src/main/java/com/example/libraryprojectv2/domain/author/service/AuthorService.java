package com.example.libraryprojectv2.domain.author.service;

import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.mapper.AuthorMapper;
import com.example.libraryprojectv2.domain.author.model.Author;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static java.text.MessageFormat.format;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Transactional
    public AuthorDto createAuthor(final AuthorDto authorDto) {
        final Author authorToBeSaved = authorMapper.authorDtoToAuthor(authorDto);
        final Author savedAuthor = authorRepository.save(authorToBeSaved);
        final AuthorDto savedAuthorDto = authorMapper.authorToAuthorDto(savedAuthor);
        return savedAuthorDto;
    }

    public AuthorDto getAuthorById(final long id) {
        final Author author = authorRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Author with id of {0} not found!", id)
                ));

        final AuthorDto authorDto = authorMapper.authorToAuthorDto(author);
        return authorDto;
    }
}
