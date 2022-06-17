package com.example.libraryprojectv2.domain.book.mapper;

import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dto.BookDataWithIsbnDto;
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


    default Book bookDataWithIsbnDtoToBook(final BookDataWithIsbnDto bookDataWithIsbnDto) {
        if (isNull(bookDataWithIsbnDto)) {
            return null;
        }

        final Set<Author> authors = new HashSet<>();

        return new Book(bookDataWithIsbnDto.getIsbnId(), bookDataWithIsbnDto.getTitle(), authors);
    }


    default BookDataWithIsbnDto bookToBookDataWithIsbnDto(final Book book) {
        if (isNull(book)) {
            return null;
        }

        return new BookDataWithIsbnDto(book.getTitle(), book.getIsbnId());
    }


    default Book bookIsbnDtoToBook(final BookIsbnDto bookIsbnDto) {
        if (isNull(bookIsbnDto)) {
            return null;
        }

        final Set<Author> authors = new HashSet<>();

        return new Book(bookIsbnDto.isbnId(), null, authors);
    }
}
