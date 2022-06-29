package com.example.libraryprojectv2.domain.mapper;

import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dto.*;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherDataDto;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherDto;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherIdDto;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper { // All mapping methods are in one class to avoid cycling


    Mapper INSTANCE = Mappers.getMapper(Mapper.class);


//    ========================================== 'Author' entity ===============================================


    @Named("authorIdDtoToAuthor")
    Author authorIdDtoToAuthor(AuthorIdDto authorIdDto); // authorIdDto -> author


    @Named("authorToAuthorIdDto")
    @InheritInverseConfiguration(name = "authorIdDtoToAuthor")
    AuthorIdDto authorToAuthorIdDto(Author author); // author -> authorIdDto


    @Mapping(source = "books", target = "books", qualifiedByName = "bookSetToBookIdDtoSet")
    AuthorDto authorToAuthorDto(Author author); // author -> authorDto


    @Named("authorSetToAuthorIdDtoSet")
    @IterableMapping(qualifiedByName = "authorToAuthorIdDto")
    Set<AuthorIdDto> authorSetToAuthorIdDtoSet(Set<Author> authors);


//    ========================================== 'Book' entity ===============================================


    @Named("bookIdDtoToBook")
    @Mapping(source = "publisher", target = "publisher", qualifiedByName = "publisherIdDtoToPublisher")
    Book bookIdDtoToBook(BookIdDto bookIdDto); // bookIdDto -> book


    @Named("bookToBookIdDto")
    @InheritInverseConfiguration(name = "bookIdDtoToBook")
    @Mapping(source = "publisher", target = "publisher", qualifiedByName = "publisherToPublisherIdDto")
    BookIdDto bookToBookIdDto(Book book); // book -> bookIdDto


    @Mapping(source = "authors", target = "authors", qualifiedByName = "authorSetToAuthorIdDtoSet")
    @Mapping(source = "publisher", target = "publisher", qualifiedByName = "publisherToPublisherIdDto")
    BookDto bookToBookDto(Book book); // book -> bookDto


    @Named("bookToBookDataDto")
    @Mapping(source = "publisher", target = "publisher", qualifiedByName = "publisherToPublisherIdDto")
    BookDataDto bookToBookDataDto(Book book); // book -> bookDataDto


    @InheritInverseConfiguration(name = "bookToBookDataDto")
    @Mapping(source = "publisher", target = "publisher", qualifiedByName = "publisherIdDtoToPublisher")
    Book bookDataDtoToBook(BookDataDto bookDataDto); // bookDataDto -> book


    Book bookIsbnIdDtoToBook(BookIsbnIdDto bookIsbnIdDto); // bookIsbnIdDto -> book


    Book bookInitDtoToBook(BookInitDto bookInitDto); // bookInitDto -> book


    @Named("bookFullInfoDtoToBook")
    @Mapping(source = "authors", target = "authors", qualifiedByName = "authorIdDtoToAuthor")
    Book bookFullInfoDtoToBook(BookFullInfoDto bookFullInfoDto); // bookFullInfoDto -> book

    @InheritInverseConfiguration(name = "bookFullInfoDtoToBook")
    @Mapping(source = "authors", target = "authors", qualifiedByName = "authorSetToAuthorIdDtoSet")
    BookFullInfoDto bookToBookFullInfoDto(Book book); // book -> bookFullInfoDto


    @Named("bookSetToBookIdDtoSet")
    @IterableMapping(qualifiedByName = "bookToBookIdDto")
    Set<BookIdDto> bookSetToBookIdDtoSet(Set<Book> books);


//        ========================================== 'Publisher' entity ===============================================


    @Named("publisherDtoToPublisher")
    Publisher publisherDtoToPublisher(PublisherDto publisherDto); // publisherDto -> publisher


    @Named("publisherToPublisherDto")
    @InheritInverseConfiguration(name = "publisherDtoToPublisher")
    PublisherDto publisherToPublisherDto(Publisher publisher); // publisher -> publisherDto


    @Named("publisherIdDtoToPublisher")
    Publisher publisherIdDtoToPublisher(PublisherIdDto publisherIdDto); // publisherIdDto -> publisher


    @Named("publisherToPublisherIdDto")
    @InheritInverseConfiguration(name = "publisherIdDtoToPublisher")
    PublisherIdDto publisherToPublisherIdDto(Publisher publisher); // publisher -> publisherIdDto


    Publisher publisherDataDtoToPublisher(PublisherDataDto publisherDataDto); // publisherDataDto -> publisher
}
