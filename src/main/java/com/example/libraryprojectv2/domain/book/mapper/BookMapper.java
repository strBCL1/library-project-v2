package com.example.libraryprojectv2.domain.book.mapper;

import com.example.libraryprojectv2.domain.author.dto.AuthorNameDto;
import com.example.libraryprojectv2.domain.author.mapper.AuthorMapper;
import com.example.libraryprojectv2.domain.book.dto.BookDto;
import com.example.libraryprojectv2.domain.book.dto.BookTitleDto;
import com.example.libraryprojectv2.domain.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    default Book bookTitleDtoToBook(final BookTitleDto bookTitleDto) {
        if (isNull(bookTitleDto)) {
            return null;
        }

        return Book.BookBuilder
                .aBook()
                .title(bookTitleDto.title())
                .build();
    }

    default BookTitleDto bookToBookTitleDto(final Book book) {
        if (isNull(book)) {
            return null;
        }

        return new BookTitleDto(book.getTitle());
    }

    default BookDto bookToBookDto(final Book book) {
        if (isNull(book)) {
            return null;
        }

        final Set<AuthorNameDto> authorNameDtos = book
                .getAuthors()
                .stream()
                .map(AuthorMapper.INSTANCE::authorToAuthorNameDto)
                .collect(Collectors.toSet());

        return new BookDto(book.getTitle(), authorNameDtos);
    }
}
