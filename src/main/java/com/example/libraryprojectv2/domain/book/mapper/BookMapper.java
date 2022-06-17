package com.example.libraryprojectv2.domain.book.mapper;

import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.book.dto.BookIsbnDto;
import com.example.libraryprojectv2.domain.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    default Book bookDataDtoToBook(final BookDataDto bookDataDto) {
        if (isNull(bookDataDto)) {
            return null;
        }

        final Set<Author> authors = new HashSet<>();

        return new Book(bookDataDto.getIsbnId(), bookDataDto.getTitle(), authors);
    }

    default BookDataDto bookToBookDataDto(final Book book) {
        if (isNull(book)) {
            return null;
        }

        return new BookDataDto(book.getIsbnId(), book.getTitle());
    }

    default Book BookIsbnDtoToBook(final BookIsbnDto bookIsbnDto) {
        if (isNull(bookIsbnDto)) {
            return null;
        }

        final Set<Author> authors = new HashSet<>();

        return new Book(bookIsbnDto.getIsbnId(), null, authors);
    }
}
