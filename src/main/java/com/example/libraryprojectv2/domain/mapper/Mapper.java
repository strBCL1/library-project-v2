package com.example.libraryprojectv2.domain.mapper;

import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.book.dto.BookDto;
import com.example.libraryprojectv2.domain.book.dto.BookIdDto;
import com.example.libraryprojectv2.domain.book.dto.BookTitleDto;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherDto;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper { // All mapping methods in one class to avoid cycling


    Mapper INSTANCE = Mappers.getMapper(Mapper.class);


//    ========================================== 'Author' entity ===============================================


    Author authorIdDtoToAuthor(AuthorIdDto authorIdDto); // authorIdDto -> author


    @InheritInverseConfiguration
    AuthorIdDto authorToAuthorIdDto(Author author); // author -> authorIdDto


    @Mapping(source = "books", target = "books", qualifiedByName = "bookSetToBookDataDtoSet")
    AuthorDto authorToAuthorDto(Author author); // author -> authorDto


    @Named("authorToAuthorDataDto")
    AuthorDataDto authorToAuthorDataDto(Author author); // author -> authorDataDto


    @Named("authorSetToAuthorDataDtoSet")
    @IterableMapping(qualifiedByName = "authorToAuthorDataDto")
    Set<AuthorDataDto> authorSetToAuthorDataDtoSet(Set<Author> books);


//    ========================================== 'Book' entity ===============================================


    Book bookIdDtoToBook(BookIdDto bookIdDto); // bookIdDto -> book


    @Named("bookToBookIdDto")
    @InheritInverseConfiguration
    BookIdDto bookToBookIdDto(Book book); // book -> bookIdDto


    @Mapping(source = "authors", target = "authors", qualifiedByName = "authorSetToAuthorDataDtoSet")
    BookDto bookToBookDto(Book book); // book -> bookDto


    @Named("bookToBookDataDto")
    BookDataDto bookToBookDataDto(Book book); // book -> bookDataDto


    @InheritInverseConfiguration(name = "bookToBookDataDto")
    Book bookDataDtoToBook(BookDataDto bookDataDto); // bookDataDto -> book


    Book bookTitleDtoToBook(BookTitleDto bookTitleDto); // bookTitleDto -> book


    @InheritInverseConfiguration
    BookTitleDto bookToBookTitleDto(Book book); // book -> bookTitleDto


    @Named("bookSetToBookDataDtoSet")
    @IterableMapping(qualifiedByName = "bookToBookDataDto")
    Set<BookDataDto> bookSetToBookDataDtoSet(Set<Book> books);


//        ========================================== 'Publisher' entity ===============================================


    Publisher publisherDtoToPublisher(PublisherDto publisherDto); // publisherDto -> publisher


    @InheritInverseConfiguration
    PublisherDto publisherToPublisherDto(Publisher publisher); // publisher -> publisherIdDto
}
