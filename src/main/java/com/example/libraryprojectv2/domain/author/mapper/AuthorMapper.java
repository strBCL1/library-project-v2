package com.example.libraryprojectv2.domain.author.mapper;

import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.book.mapper.BookMapper;
import com.example.libraryprojectv2.domain.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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

        return Author.builder()
                .orcidId(authorDto.getOrcidId())
                .firstName(authorDto.getFirstName())
                .lastName(authorDto.getLastName())
                .books(books)
                .build();
    }

    default Author authorDataDtoToAuthor(final AuthorDataDto authorDataDto) {
        if (isNull(authorDataDto)) {
            return null;
        }

        return Author.builder()
                .orcidId(authorDataDto.getOrcidId())
                .firstName(authorDataDto.getFirstName())
                .lastName(authorDataDto.getLastName())
                .build();
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
