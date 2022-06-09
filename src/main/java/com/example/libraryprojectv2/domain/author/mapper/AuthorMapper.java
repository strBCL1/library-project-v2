package com.example.libraryprojectv2.domain.author.mapper;

import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorNameDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dto.BookTitleDto;
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
                .bookTitleDtos()
                .stream()
                .map(BookMapper.INSTANCE::bookTitleDtoToBook)
                .collect(Collectors.toSet());

        return Author.AuthorBuilder
                .anAuthor()
                .firstName(authorDto.firstName())
                .lastName(authorDto.lastName())
                .books(books)
                .build();
    }

    default AuthorNameDto authorToAuthorNameDto(final Author author) {
        if (isNull(author)) {
            return null;
        }

        return new AuthorNameDto(author.getFirstName(), author.getLastName());
    }

    default AuthorDto authorToAuthorDto(final Author author) {
        if (isNull(author)) {
            return null;
        }

        final Set<BookTitleDto> bookTitleDtos = author
                .getBooks()
                .stream()
                .map(BookMapper.INSTANCE::bookToBookTitleDto)
                .collect(Collectors.toSet());

        return new AuthorDto(author.getFirstName(), author.getLastName(), bookTitleDtos);
    }
}
