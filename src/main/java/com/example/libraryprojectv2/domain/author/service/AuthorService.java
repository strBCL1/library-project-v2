package com.example.libraryprojectv2.domain.author.service;

import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.mapper.AuthorMapper;
import com.example.libraryprojectv2.domain.author.model.Author;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import java.util.Optional;

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
}
