package com.example.libraryprojectv2.domain.mapper;

import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.dto.*;
import com.example.libraryprojectv2.domain.book.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperTest {

    private final static String VALID_FIRST_NAME = "firstName";
    private final static String VALID_LAST_NAME = "lastName";
    private final static String VALID_ORCID_ID = "1111111111111111";

    private final static String VALID_ISBN_ID = "1111111111111";
    private final static String VALID_TITLE = "title";

    private final static Long VALID_ID = 1L;
    private final static String VALID_NAME = "name";
    private final static String VALID_ADDRESS = "address";
    private final static String VALID_CITY = "city";
    private final static String VALID_COUNTRY = "country";

    @Spy
    Mapper mapper = Mapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


//    ========================================== 'Author' entity ===============================================


    @Test
    void givenAuthorIdDto_whenAuthorIdDtoToAuthor_thenAuthorIdDtoEqualsToAuthor() { // authorIdDto -> author
        final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID);
        final Author author = mapper.authorIdDtoToAuthor(authorIdDto);

        assertAll("Author's fields must be equal to authorIdDto's fields",
                () -> assertEquals(authorIdDto.getOrcidId(), author.getOrcidId()),
                () -> assertEquals(authorIdDto.getFirstName(), author.getFirstName()),
                () -> assertEquals(authorIdDto.getLastName(), author.getLastName()));
    }

    @Test
    void givenAuthor_whenAuthorToAuthorIdDto_thenAuthorEqualsToAuthorIdDto() { // author -> authorIdDto
        final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, Collections.emptySet());
        final AuthorIdDto authorIdDto = mapper.authorToAuthorIdDto(author);

        assertAll("AuthorIdDto's fields must be equal to author's fields",
                () -> assertEquals(author.getOrcidId(), authorIdDto.getOrcidId()),
                () -> assertEquals(author.getFirstName(), authorIdDto.getFirstName()),
                () -> assertEquals(author.getLastName(), authorIdDto.getLastName()));
    }

    @Test
    void givenAuthor_whenAuthorToAuthorDto_thenAuthorEqualsToAuthorDto() { // author -> authorDto
        final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, Collections.emptySet());
        final AuthorDto authorDto = mapper.authorToAuthorDto(author);

        assertAll("Author's fields must be equal to authorDto's fields",
                () -> assertEquals(author.getOrcidId(), authorDto.getOrcidId()),
                () -> assertEquals(author.getFirstName(), authorDto.getFirstName()),
                () -> assertEquals(author.getLastName(), authorDto.getLastName()),
                () -> assertEquals(author.getBooks().size(), authorDto.getBooks().size()));
    }

    @Test
    void givenAuthorsSet_whenAuthorSetToAuthorIdDtoSet_thenAuthorsSetEqualsToAuthorIdDtoSet() { // 'Author' Set -> 'AuthorIdDto' set
        final Set<Author> authors = Set.of(
                new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, Collections.emptySet())
        );
        final Set<AuthorIdDto> authorIdDtos = mapper.authorSetToAuthorIdDtoSet(authors);

        assertAll("Authors set's elements must equal to AuthorIdDto set's elements",
                () -> assertEquals(authors.size(), authorIdDtos.size()),
                () -> {
            final Iterator<Author> authorIterator = authors.iterator();
            final Iterator<AuthorIdDto> authorIdDtoIterator = authorIdDtos.iterator();

            while (authorIterator.hasNext() && authorIdDtoIterator.hasNext()) {
                final Author author = authorIterator.next();
                final AuthorIdDto authorIdDto = authorIdDtoIterator.next();

                assertAll("Author's fields must equal to AuthorIdDto's fields",
                        () -> assertEquals(author.getOrcidId(), authorIdDto.getOrcidId()),
                        () -> assertEquals(author.getFirstName(), authorIdDto.getFirstName()),
                        () -> assertEquals(author.getLastName(), authorIdDto.getLastName()));
            }
                });
    }


//    ========================================== 'Book' entity ===============================================


    @Test
    void givenBookIdDto_whenBookIdDtoToBook_thenBookIdDtoEqualsToBook() { // bookIdDto -> book
        final BookIdDto bookIdDto = new BookIdDto(VALID_TITLE, null, VALID_ISBN_ID);
        final Book book = mapper.bookIdDtoToBook(bookIdDto);

        assertAll("BookIdDto's fields must equal to Book's fields",
                () -> assertEquals(bookIdDto.getIsbnId(), book.getIsbnId()),
                () -> assertEquals(bookIdDto.getTitle(), book.getTitle()));
    }

    @Test
    void givenBook_whenBookToBookIdDto_thenBookEqualsToBookIdDto() { // book -> bookIdDto
        final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, Collections.emptySet(), null);
        final BookIdDto bookIdDto = mapper.bookToBookIdDto(book);

        assertAll("Book's fields must equal to BookIdDto's fields",
                () -> assertEquals(book.getIsbnId(), bookIdDto.getIsbnId()),
                () -> assertEquals(book.getTitle(), bookIdDto.getTitle()));
    }

    @Test
    void givenBook_whenBookToBookDto_thenBookEqualsToBookDto() { // book -> bookDto
        final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, Collections.emptySet(), null);
        final BookDto bookDto = mapper.bookToBookDto(book);

        assertAll("Book's fields must equal to BookDto's fields",
                () -> assertEquals(book.getIsbnId(), bookDto.getIsbnId()),
                () -> assertEquals(book.getTitle(), bookDto.getTitle()));
    }

    @Test
    void givenBook_whenBookToBookDataDto_thenBookEqualsToBookDataDto() { // book -> bookDataDto
        final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, Collections.emptySet(), null);
        final BookDataDto bookDataDto = mapper.bookToBookDataDto(book);

        assertAll("Book's fields must equal to BookDataDto's fields",
                () -> assertEquals(book.getTitle(), bookDataDto.getTitle()));
    }

    @Test
    void givenBookDataDto_whenBookDataDtoToBook_thenBookDataDtoEqualsToBook() { // bookDataDto -> book
        final BookDataDto bookDataDto = new BookDataDto(VALID_TITLE, null);
        final Book book = mapper.bookDataDtoToBook(bookDataDto);

        assertAll("BookDataDto's fields must equal to Book's fields",
                () -> assertEquals(bookDataDto.getTitle(), book.getTitle()));
    }

    @Test
    void givenBookIsbnIdDto_whenBookIsbnIdDtoToBook_thenBookIsbnIdDtoEqualsToBook() { // bookIsbnIdDto -> book
        final BookIsbnIdDto bookIsbnIdDto = new BookIsbnIdDto(VALID_ISBN_ID);
        final Book book = mapper.bookIsbnIdDtoToBook(bookIsbnIdDto);

        assertAll("BookIsbnIdDto's fields must equal to Book's fields",
                () -> assertEquals(bookIsbnIdDto.isbnId(), book.getIsbnId()));
    }

    @Test
    void givenBookInitDto_whenBookInitDtoToBook_thenBookInitDtoEqualsToBook() { // bookInitDto -> book
        final BookInitDto bookInitDto = new BookInitDto(VALID_ISBN_ID, VALID_TITLE, null);
        final Book book = mapper.bookInitDtoToBook(bookInitDto);

        assertAll("BookInitDto's fields must equal to Book's fields",
                () -> assertEquals(bookInitDto.isbnId(), book.getIsbnId()),
                () -> assertEquals(bookInitDto.title(), book.getTitle()));
    }

    @Test
    void givenBookFullInfoDto_whenBookFullInfoDtoToBook_thenBookFullInfoDtoEqualsToBook() { // bookFullInfoDto -> book
        final BookFullInfoDto bookFullInfoDto = new BookFullInfoDto(VALID_ISBN_ID, VALID_TITLE, Collections.emptySet());
        final Book book = mapper.bookFullInfoDtoToBook(bookFullInfoDto);

        assertAll("BookFullInfoDto's fields must equal to Book's fields",
                () -> assertEquals(bookFullInfoDto.isbnId(), book.getIsbnId()),
                () -> assertEquals(bookFullInfoDto.title(), book.getTitle()));
    }

    @Test
    void givenBooksSet_whenBookSetToBookIdDtoSet_thenBooksSetEqualsToBookIdDtoSet() { // 'Book' set -> 'BookIdDto' set
        final Set<Book> books = Set.of(
                new Book(VALID_ISBN_ID, VALID_TITLE, Collections.emptySet(), null)
        );
        final Set<BookIdDto> bookIdDtos = mapper.bookSetToBookIdDtoSet(books);

        assertAll("Book set's elements must equal to BookIdDto set's elements",
                () -> assertEquals(books.size(), bookIdDtos.size()),
                () -> {
            final Iterator<Book> bookIterator = books.iterator();
            final Iterator<BookIdDto> bookIdDtoIterator = bookIdDtos.iterator();

            while (bookIterator.hasNext() && bookIdDtoIterator.hasNext()) {
                final Book book = bookIterator.next();
                final BookIdDto bookIdDto = bookIdDtoIterator.next();

                assertAll("Book's fields must equal to BookIdDto's fields",
                        () -> assertEquals(book.getIsbnId(), bookIdDto.getIsbnId()),
                        () -> assertEquals(book.getTitle(), bookIdDto.getTitle()));
            }
                });
    }
}