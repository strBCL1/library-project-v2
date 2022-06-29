package com.example.libraryprojectv2.domain.mapper;

import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.HashSet;
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

    private final static Set<Book> VALID_BOOKS = new HashSet<>();
    private final static Set<Author> VALID_AUTHORS = new HashSet<>();

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
    void givenAuthorIdDto_whenAuthorIdDtoToAuthor_thenAuthorIdDtoEqualsToAuthor() { // authorIdDtoToAuthor
        final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID);
        final Author author = mapper.authorIdDtoToAuthor(authorIdDto);

        assertAll("Author's fields must be equal to authorIdDto's fields",
                () -> assertEquals(authorIdDto.getOrcidId(), author.getOrcidId()),
                () -> assertEquals(authorIdDto.getFirstName(), author.getFirstName()),
                () -> assertEquals(authorIdDto.getLastName(), author.getLastName()));
    }

    @Test
    void givenAuthor_whenAuthorToAuthorIdDto_thenAuthorEqualsToAuthorIdDto() { // authorToAuthorIdDto
        final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, Collections.emptySet());
        final AuthorIdDto authorIdDto = mapper.authorToAuthorIdDto(author);

        assertAll("AuthorIdDto's fields must be equal to author's fields",
                () -> assertEquals(author.getOrcidId(), authorIdDto.getOrcidId()),
                () -> assertEquals(author.getFirstName(), authorIdDto.getFirstName()),
                () -> assertEquals(author.getLastName(), authorIdDto.getLastName()));
    }

    @Test
    void givenAuthor_whenAuthorToAuthorDto_thenAuthorEqualsToAuthorDto() { // authorToAuthorDto
        final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, Collections.emptySet());
        final AuthorDto authorDto = mapper.authorToAuthorDto(author);

        assertAll("Author's fields must be equal to authorDto's fields",
                () -> assertEquals(author.getOrcidId(), authorDto.getOrcidId()),
                () -> assertEquals(author.getFirstName(), authorDto.getFirstName()),
                () -> assertEquals(author.getLastName(), authorDto.getLastName()),
                () -> assertEquals(author.getBooks().size(), authorDto.getBooks().size()));
    }

    @Test
    void givenAuthorsSet_whenAuthorSetToAuthorIdDtoSet_thenAuthorsSetEqualsToAuthorIdDtoSet() { // authorSetToAuthorIdDtoSet_
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
}