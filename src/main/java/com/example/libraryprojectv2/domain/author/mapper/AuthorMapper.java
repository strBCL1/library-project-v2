package com.example.libraryprojectv2.domain.author.mapper;

import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.book.mapper.BookMapper;
import com.example.libraryprojectv2.domain.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    default Author authorDtoToAuthor(final AuthorDto authorDto) {
        if (isNull(authorDto)) {
            return null;
        }

        final Set<Book> books = authorDto
                .getBookDataDtos()
                .stream()
                .map(BookMapper.INSTANCE::bookDataDtoToBook)
                .collect(Collectors.toSet());

        return new Author(authorDto.getOrcidId(), authorDto.getFirstName(), authorDto.getLastName(), books);
    }

    default Author authorDataDtoToAuthor(final AuthorDataDto authorDataDto) {
        if (isNull(authorDataDto)) {
            return null;
        }

        final Set<Book> books = new HashSet<>();

        return new Author(authorDataDto.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), books);
    }

    default AuthorDataDto authorToAuthorDataDto(final Author author) {
        if (isNull(author)) {
            return null;
        }

        return new AuthorDataDto(author.getOrcidId(), author.getFirstName(), author.getLastName());
    }

    default AuthorDto authorToAuthorDto(final Author author) {
        if (isNull(author)) {
            return null;
        }

        final Set<BookDataDto> bookTitleDtos = author
                .getBooks()
                .stream()
                .map(BookMapper.INSTANCE::bookToBookDataDto)
                .collect(Collectors.toSet());

        return new AuthorDto(author.getOrcidId(), author.getFirstName(), author.getLastName(), bookTitleDtos);
    }
}
