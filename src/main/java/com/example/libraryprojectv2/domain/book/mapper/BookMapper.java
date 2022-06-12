package com.example.libraryprojectv2.domain.book.mapper;

import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    default Book bookDataDtoToBook(final BookDataDto bookDataDto) {
        if (isNull(bookDataDto)) {
            return null;
        }

        return Book.builder()
                .isbnId(bookDataDto.getIsbnId())
                .title(bookDataDto.getTitle())
                .build();
    }

    default BookDataDto bookToBookDataDto(final Book book) {
        if (isNull(book)) {
            return null;
        }

        return new BookDataDto(book.getIsbnId(), book.getTitle());
    }
}
