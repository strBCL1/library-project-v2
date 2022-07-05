package com.example.libraryprojectv2.domain.author.dao;

import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorRepositoryTest {

    private final static String VALID_FIRST_NAME = "firstName";
    private final static String VALID_LAST_NAME = "lastName";
    private final static String VALID_ORCID_ID = "1111111111111111";

    private final static String VALID_ISBN_ID = "1111111111111";
    private final static String VALID_TITLE = "title";
    private final static String NEW_ISBN_ID = VALID_ISBN_ID.replace(VALID_ISBN_ID.charAt(0), (char) (VALID_ISBN_ID.charAt(0) + 1));
    private final static String NEW_TITLE = VALID_TITLE.repeat(2);

    private final static Long VALID_ID = 1L;
    private final static String VALID_NAME = "name";
    private final static String VALID_ADDRESS = "address";
    private final static String VALID_CITY = "city";
    private final static String VALID_COUNTRY = "country";

    private Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
    private Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
    private Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());


    @Autowired
    private AuthorRepository authorRepository;


    @Test
    void givenValidAuthor_whenSave_thenReturnValidAuthor() {
        final Author validAuthor = authorRepository.save(author);

        assertAll("Valid author must contain same fields as saved author",
                () -> assertEquals(author.getOrcidId(), validAuthor.getOrcidId()),
                () -> assertEquals(author.getFirstName(), validAuthor.getFirstName()),
                () -> assertEquals(author.getLastName(), validAuthor.getLastName()),
                () -> assertEquals(author.getBooks(), validAuthor.getBooks()));
    }


    @Test
    void givenAuthorWithLongOrcidId_whenSave_thenThrowConstraintViolationException() {
        final Author longOrcidIdAuthor = new Author(copy(author.getOrcidId()), author.getFirstName(), author.getLastName(), author.getBooks());

        final TransactionSystemException transactionSystemException = assertThrows(TransactionSystemException.class, () -> authorRepository.save(longOrcidIdAuthor));

        assertAll("TransactionSystemException must contain ConstraintValidationException and an error message",
                () -> assertEquals(ConstraintViolationException.class, transactionSystemException.getRootCause().getClass()),
                () -> {
                    final Throwable constraintViolationException = transactionSystemException.getRootCause();
                    assertTrue(constraintViolationException.getMessage().contains("Author's ORCID code must only have digits of length of 16!"));
                });
    }


    @Test
    void givenAuthorWithShortOrcidId_whenSave_thenThrowConstraintViolationException() {
        final Author shortOrcidIdAuthor = new Author(reduce(author.getOrcidId()), author.getFirstName(), author.getLastName(), author.getBooks());

        final TransactionSystemException transactionSystemException = assertThrows(TransactionSystemException.class, () -> authorRepository.save(shortOrcidIdAuthor));

        assertAll("TransactionSystemException must contain ConstraintValidationException and an error message",
                () -> assertEquals(ConstraintViolationException.class, transactionSystemException.getRootCause().getClass()),
                () -> {
                    final Throwable constraintViolationException = transactionSystemException.getRootCause();
                    assertTrue(constraintViolationException.getMessage().contains("Author's ORCID code must only have digits of length of 16!"));
                });
    }


    @Test
    void givenAuthorWithInvalidCharactersOrcidId_whenSave_thenThrowConstraintViolationException() {
        final Author shortOrcidIdAuthor = new Author(fillWithInvalidCharacters(author.getOrcidId()), author.getFirstName(), author.getLastName(), author.getBooks());

        final TransactionSystemException transactionSystemException = assertThrows(TransactionSystemException.class, () -> authorRepository.save(shortOrcidIdAuthor));

        assertAll("TransactionSystemException must contain ConstraintValidationException and an error message",
                () -> assertEquals(ConstraintViolationException.class, transactionSystemException.getRootCause().getClass()),
                () -> {
                    final Throwable constraintViolationException = transactionSystemException.getRootCause();
                    assertTrue(constraintViolationException.getMessage().contains("Author's ORCID code must only have digits of length of 16!"));
                });
    }


    private String reduce(final String string) {
        return string.substring(string.length() - 1);
    }


    private String copy(final String string) {
        return string.repeat(string.matches("\\d+") ? 2 : 10);
    }


    private String fillWithInvalidCharacters(final String string) {
        return string.replace(
                string.charAt(0),
                string.matches("\\d+") ? 'A' : '1'
        );
    }


    private String createUnknownValidId(final String id) {
        final char firstChar = id.charAt(0);
        return id.replace(firstChar, firstChar == '9' ? '0' : (char) (firstChar + 1));
    }

}