package com.example.libraryprojectv2.domain.author.controller;

import com.example.libraryprojectv2.configuration.response.cause.Cause;
import com.example.libraryprojectv2.configuration.response.handler.RestExceptionHandler;
import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.BookIsbnIdDto;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthorControllerTest {

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
    private AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID);

    private AuthorDataDto authorDataDto = new AuthorDataDto(author.getFirstName().repeat(2), author.getLastName().repeat(2));
    private Author updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());
    private List<Book> booksList = new ArrayList<>();
    private final Book newBook = new Book(NEW_ISBN_ID, NEW_TITLE, new HashSet<>(), null);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @SpyBean
    private Mapper mapper;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @SpyBean
    private AuthorService authorService;

    @SpyBean
    private AuthorController authorController;

    @SpyBean
    private Cause cause;

    @SpyBean
    private RestExceptionHandler restExceptionHandler;

    @SpyBean
    private MethodValidationPostProcessor methodValidationPostProcessor;

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        author.addBook(book);
        book.updatePublisher(publisher);

        author.addBook(newBook);
        newBook.updatePublisher(publisher);

        booksList.add(book);
        booksList.add(newBook);
    }


    @Nested
    class AuthorControllerGetTest {


        @BeforeEach
        void setUp() {
            when(authorRepository.findById(VALID_ORCID_ID)).thenReturn(Optional.of(author));
        }


        private void shouldReturnInvalidOrcidIdMessageWhenGet(final String URL) throws Exception {
            mockMvc.perform(get(URL))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Author's ORCID code must only have digits of length of 16!")));
        }


//    ======================================= GET author by ORCID id =======================================


        @Test
        void givenValidOrcidId_whenGetAuthorByOrcidId_thenReturnValidAuthor() throws Exception {

            mockMvc.perform(get("/authors/" + author.getOrcidId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orcidId", equalTo(author.getOrcidId())))
                    .andExpect(jsonPath("$.firstName", equalTo(author.getFirstName())))
                    .andExpect(jsonPath("$.lastName", equalTo(author.getLastName())))
                    .andExpect(jsonPath("$.books", hasSize(author.getBooks().size())))
                    .andExpect(jsonPath("$.books.[0].isbnId", equalTo(book.getIsbnId())))
                    .andExpect(jsonPath("$.books.[0].title", equalTo(book.getTitle())))
                    .andExpect(jsonPath("$.books.[0].publisher.id", equalTo(publisher.getId().intValue())))
                    .andExpect(jsonPath("$.books.[0].publisher.name", equalTo(publisher.getName())))
                    .andExpect(jsonPath("$.books.[0].publisher.address", equalTo(publisher.getAddress())))
                    .andExpect(jsonPath("$.books.[0].publisher.city", equalTo(publisher.getCity())))
                    .andExpect(jsonPath("$.books.[0].publisher.country", equalTo(publisher.getCountry())))
                    .andExpect(jsonPath("$.books.[0].publisher.books").doesNotExist());
        }


        @Test
        void givenShortOrcidId_whenGetAuthorByOrcidId_thenReturnOrcidIdErrorMessage() throws Exception {
            shouldReturnInvalidOrcidIdMessageWhenGet("/authors/" +
                    VALID_ORCID_ID.substring(VALID_ORCID_ID.length() - 1));
        }


        @Test
        void givenLongOrcidId_whenGetAuthorByOrcidId_thenReturnOrcidIdErrorMessage() throws Exception {
            shouldReturnInvalidOrcidIdMessageWhenGet("/authors/" +
                    VALID_ORCID_ID.repeat(2));
        }


        @Test
        void givenOrcidIdWithInvalidCharacters_whenGetAuthorByOrcidId_thenReturnOrcidIdErrorMessage() throws Exception {
            shouldReturnInvalidOrcidIdMessageWhenGet("/authors/" +
                    VALID_ORCID_ID.replace(VALID_ORCID_ID.charAt(0), 'A'));
        }


        @Test
        void givenUnknownValidOrcidId_whenGetAuthorByOrcidId_thenReturnAuthorNotFoundErrorMessage() throws Exception {
            final String unknownValidOrcidId = VALID_ORCID_ID
                    .replace(VALID_ORCID_ID.charAt(0), (char) (VALID_ORCID_ID.charAt(0) + 1));

            mockMvc.perform(get("/authors/" + unknownValidOrcidId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.cause", equalTo("Author with ORCID code of " + unknownValidOrcidId + " not found!")));
        }


//    ======================================= GET authors =======================================


        @Test
        void givenAuthors_whenGetAuthors_thenReturnAuthors() throws Exception {
            final Author author1 = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
            final Author author2 = new Author(VALID_ORCID_ID.replace(VALID_ORCID_ID.charAt(0), (char) (VALID_ORCID_ID.charAt(0) + 1)), VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());

            author1.addBook(book);
            author2.addBook(book);
            book.updatePublisher(publisher);

            final List<Author> authors = List.of(author1, author2);

            when(authorRepository.findAll()).thenReturn(authors);

            mockMvc.perform(get("/authors"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(authors.size())))
                    .andExpect(jsonPath("$.[0].orcidId", equalTo(author1.getOrcidId())))
                    .andExpect(jsonPath("$.[0].firstName", equalTo(author1.getFirstName())))
                    .andExpect(jsonPath("$.[0].lastName", equalTo(author1.getLastName())))
                    .andExpect(jsonPath("$.[0].books", hasSize(1)))
                    .andExpect(jsonPath("$.[0].books.[0].isbnId", equalTo(book.getIsbnId())))
                    .andExpect(jsonPath("$.[0].books.[0].title", equalTo(book.getTitle())))
                    .andExpect(jsonPath("$.[0].books.[0].publisher.id", equalTo(publisher.getId().intValue())))
                    .andExpect(jsonPath("$.[0].books.[0].publisher.name", equalTo(publisher.getName())))
                    .andExpect(jsonPath("$.[0].books.[0].publisher.address", equalTo(publisher.getAddress())))
                    .andExpect(jsonPath("$.[0].books.[0].publisher.city", equalTo(publisher.getCity())))
                    .andExpect(jsonPath("$.[0].books.[0].publisher.country", equalTo(publisher.getCountry())))
                    .andExpect(jsonPath("$.[0].books.[0].publisher.books").doesNotExist());
        }
    }


    @Nested
    class AuthorControllerPostTest {


        private void shouldReturnInvalidFieldMessageWhenPost(final String body, final String cause) throws Exception {
            mockMvc.perform(post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo(cause)));
        }


//    ============================================== POST author ====================================================


        @Test
        void givenValidAuthorIdDto_whenCreateAuthor_thenReturnAuthorIdDto() throws Exception {

            when(authorRepository.findById(authorIdDto.getOrcidId())).thenReturn(Optional.empty());
            when(authorRepository.save(any(Author.class))).thenReturn(author);

            mockMvc.perform(post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorIdDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.orcidId", equalTo(authorIdDto.getOrcidId())))
                    .andExpect(jsonPath("$.firstName", equalTo(authorIdDto.getFirstName())))
                    .andExpect(jsonPath("$.lastName", equalTo(authorIdDto.getLastName())))
                    .andExpect(jsonPath("$.books").doesNotExist());
        }


        @Test
        void givenLongOrcidId_whenCreateAuthor_thenReturnOrcidIdErrorMessage() throws Exception {
            authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID.repeat(2));
            author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            shouldReturnInvalidFieldMessageWhenPost(
                    toJson(authorIdDto),
                    "Author's ORCID code must only have digits of length of 16!, rejected value: '" + authorIdDto.getOrcidId() + "'"
            );
        }


        @Test
        void givenShortOrcidId_whenCreateAuthor_thenReturnOrcidIdErrorMessage() throws Exception {
            authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID.substring(VALID_ORCID_ID.length() - 1));
            author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            shouldReturnInvalidFieldMessageWhenPost(
                    toJson(authorIdDto),
                    "Author's ORCID code must only have digits of length of 16!, rejected value: '" + authorIdDto.getOrcidId() + "'"
            );
        }


        @Test
        void givenOrcidIdWithInvalidCharacters_whenCreateAuthor_thenReturnOrcidIdErrorMessage() throws Exception {
            authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID.replace(VALID_ORCID_ID.charAt(0), 'A'));
            author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            shouldReturnInvalidFieldMessageWhenPost(
                    toJson(authorIdDto),
                    "Author's ORCID code must only have digits of length of 16!, rejected value: '" + authorIdDto.getOrcidId() + "'"
            );
        }


        @Test
        void givenExistingValidOrcidId_whenCreateAuthor_thenReturnAuthorAlreadyExistsMessage() throws Exception {
            when(authorRepository.findById(authorIdDto.getOrcidId())).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(author);

            mockMvc.perform(post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorIdDto)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.cause", equalTo("Author with ORCID code of " + authorIdDto.getOrcidId() + " already exists!")));
        }


        @Test
        void givenLongFirstName_whenCreateAuthor_thenReturnFirstNameErrorMessage() throws Exception {
            authorIdDto = new AuthorIdDto(VALID_FIRST_NAME.repeat(10), VALID_LAST_NAME, VALID_ORCID_ID);
            author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            shouldReturnInvalidFieldMessageWhenPost(
                    toJson(authorIdDto),
                    "Author's first name may only contain up to 45 letters!, rejected value: '" + authorIdDto.getFirstName() + "'"
            );
        }


        @Test
        void givenFirstNameWithInvalidCharacters_whenCreateAuthor_thenReturnFirstNameErrorMessage() throws Exception {
            authorIdDto = new AuthorIdDto(VALID_FIRST_NAME + "_1", VALID_LAST_NAME, VALID_ORCID_ID);
            author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            shouldReturnInvalidFieldMessageWhenPost(
                    toJson(authorIdDto),
                    "Author's first name may only contain up to 45 letters!, rejected value: '" + authorIdDto.getFirstName() + "'"
            );
        }


        @Test
        void givenLongLastName_whenCreateAuthor_thenReturnLastNameErrorMessage() throws Exception {
            authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME.repeat(10), VALID_ORCID_ID);
            author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            shouldReturnInvalidFieldMessageWhenPost(
                    toJson(authorIdDto),
                    "Author's last name may only contain up to 45 letters!, rejected value: '" + authorIdDto.getLastName() + "'"
            );
        }


        @Test
        void givenLastNameWithInvalidCharacters_whenCreateAuthor_thenReturnLastNameErrorMessage() throws Exception {
            authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME + "_1", VALID_ORCID_ID);
            author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            shouldReturnInvalidFieldMessageWhenPost(
                    toJson(authorIdDto),
                    "Author's last name may only contain up to 45 letters!, rejected value: '" + authorIdDto.getLastName() + "'"
            );
        }


        @Test
        void givenInvalidJsonRequestBodies_whenCreateAuthor_thenReturnCauseMessages() throws Exception {
            final String unknownPropertyJson = "{ " +
                    "\"unknownProperty\": \"mockValue\"" +
                    "}";

            final Map<String, String> bodyCauseMap = Map.of(
                    "", cause.getIsEmptyOrInvalidObject(),
                    "null", cause.getIsEmptyOrInvalidObject(),
                    "{}", cause.getFieldIsNotInitialized(),
                    "[]", cause.getIsEmptyOrInvalidObject(),
                    "[{}]", cause.getIsEmptyOrInvalidObject(),
                    "{[]}", cause.getJsonSyntaxError(),
                    "\n", cause.getJsonSyntaxError(),
                    unknownPropertyJson, cause.getFieldIsNotInitialized()
            );

            for (final Map.Entry<String, String> entry : bodyCauseMap.entrySet()) {

                String body = entry.getKey();

                mockMvc.perform(put("/authors/" + VALID_ORCID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.cause", containsString(entry.getValue())));

            }

        }
    }


    @Nested
    class AuthorControllerPutAuthorDataTest {


        @BeforeEach
        void setUp() {
            when(authorRepository.findById(VALID_ORCID_ID)).thenReturn(Optional.of(author));
        }


        private void shouldReturnInvalidFieldMessageWhenPut(final String URL, final String body, final String cause) throws Exception {
            mockMvc.perform(put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo(cause)));
        }


//    ============================================== PUT author ====================================================


        @Test
        void givenValidAuthorDataDto_whenUpdateAuthorData_thenReturnUpdatedAuthorDto() throws Exception {

            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            mockMvc.perform(put("/authors/" + author.getOrcidId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orcidId", equalTo(author.getOrcidId())))
                    .andExpect(jsonPath("$.firstName", equalTo(author.getFirstName())))
                    .andExpect(jsonPath("$.lastName", equalTo(author.getLastName())))
                    .andExpect(jsonPath("$.books", hasSize(author.getBooks().size())))
                    .andExpect(jsonPath("$.books.[0].isbnId", equalTo(book.getIsbnId())))
                    .andExpect(jsonPath("$.books.[0].title", equalTo(book.getTitle())))
                    .andExpect(jsonPath("$.books.[0].publisher.id", equalTo(publisher.getId().intValue())))
                    .andExpect(jsonPath("$.books.[0].publisher.name", equalTo(publisher.getName())))
                    .andExpect(jsonPath("$.books.[0].publisher.address", equalTo(publisher.getAddress())))
                    .andExpect(jsonPath("$.books.[0].publisher.city", equalTo(publisher.getCity())))
                    .andExpect(jsonPath("$.books.[0].publisher.country", equalTo(publisher.getCountry())))
                    .andExpect(jsonPath("$.books.[0].publisher.books").doesNotExist());
        }


        @Test
        void givenLongFirstName_whenUpdateAuthorData_thenReturnFirstNameErrorMessage() throws Exception {
            authorDataDto = new AuthorDataDto(author.getFirstName().repeat(10), author.getLastName());
            updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            shouldReturnInvalidFieldMessageWhenPut(
                    "/authors/" + author.getOrcidId(),
                    toJson(authorDataDto),
                    "Author's first name may only contain up to 45 letters!, rejected value: '" + authorDataDto.getFirstName() + "'"
            );
        }


        @Test
        void givenFirstNameWithInvalidCharacters_whenUpdateAuthorData_thenReturnFirstNameErrorMessage() throws Exception {
            authorDataDto = new AuthorDataDto(author.getFirstName() + "_1", author.getLastName());
            updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            shouldReturnInvalidFieldMessageWhenPut(
                    "/authors/" + author.getOrcidId(),
                    toJson(authorDataDto),
                    "Author's first name may only contain up to 45 letters!, rejected value: '" + authorDataDto.getFirstName() + "'"
            );
        }


        @Test
        void givenLongLastName_whenUpdateAuthorData_thenReturnLastNameErrorMessage() throws Exception {
            authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName().repeat(10));
            updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            shouldReturnInvalidFieldMessageWhenPut(
                    "/authors/" + author.getOrcidId(),
                    toJson(authorDataDto),
                    "Author's last name may only contain up to 45 letters!, rejected value: '" + authorDataDto.getLastName() + "'"
            );
        }


        @Test
        void givenLastNameWithInvalidCharacters_whenUpdateAuthorData_thenReturnLastNameErrorMessage() throws Exception {
            authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName() + "_1");
            updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            shouldReturnInvalidFieldMessageWhenPut(
                    "/authors/" + author.getOrcidId(),
                    toJson(authorDataDto),
                    "Author's last name may only contain up to 45 letters!, rejected value: '" + authorDataDto.getLastName() + "'"
            );
        }


        @Test
        void givenLongOrcidId_whenUpdateAuthorData_thenReturnOrcidIdErrorMessage() throws Exception {
            authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName());
            updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            shouldReturnInvalidFieldMessageWhenPut(
                    "/authors/" + VALID_ORCID_ID.repeat(2),
                    toJson(authorDataDto),
                    "Author's ORCID code must only have digits of length of 16!"
            );
        }


        @Test
        void givenShortOrcidId_whenUpdateAuthorData_thenReturnOrcidIdErrorMessage() throws Exception {
            authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName());
            updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            shouldReturnInvalidFieldMessageWhenPut(
                    "/authors/" + VALID_ORCID_ID.substring(VALID_ORCID_ID.length() - 1),
                    toJson(authorDataDto),
                    "Author's ORCID code must only have digits of length of 16!"
            );
        }


        @Test
        void givenOrcidIdWithInvalidCharacters_whenUpdateAuthorData_thenReturnOrcidIdErrorMessage() throws Exception {
            authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName());
            updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            shouldReturnInvalidFieldMessageWhenPut(
                    "/authors/" + VALID_ORCID_ID + "_A",
                    toJson(authorDataDto),
                    "Author's ORCID code must only have digits of length of 16!"
            );
        }


        @Test
        void givenUnknownValidOrcidId_whenUpdateAuthorData_thenReturnAuthorNotFoundMessage() throws Exception {

            authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName());
            updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            final String unknownValidOrcidId = author.getOrcidId().replace(author.getOrcidId().charAt(0), (char) (author.getOrcidId().charAt(0) + 1));

            mockMvc.perform(put("/authors/" + unknownValidOrcidId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.cause", equalTo("Author with ORCID code of " + unknownValidOrcidId + " not found!")));
        }


        @Test
        void givenInvalidJsonRequestBodies_whenUpdateAuthorData_thenReturnCauseMessages() throws Exception {
            final String unknownPropertyJson = "{ " +
                    "\"unknownProperty\": \"mockValue\"" +
                    "}";

            final Map<String, String> bodyCauseMap = Map.of(
                    "", cause.getIsEmptyOrInvalidObject(),
                    "null", cause.getIsEmptyOrInvalidObject(),
                    "{}", cause.getFieldIsNotInitialized(),
                    "[]", cause.getIsEmptyOrInvalidObject(),
                    "[{}]", cause.getIsEmptyOrInvalidObject(),
                    "{[]}", cause.getJsonSyntaxError(),
                    "\n", cause.getJsonSyntaxError(),
                    unknownPropertyJson, cause.getFieldIsNotInitialized()
            );

            for (final Map.Entry<String, String> entry : bodyCauseMap.entrySet()) {

                String body = entry.getKey();

                mockMvc.perform(put("/authors/" + VALID_ORCID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.cause", containsString(entry.getValue())));

            }

        }
    }


    @Nested
    class AuthorControllerPutAuthorBooksTest {


        @BeforeEach
        void setUp() {
            when(authorRepository.findById(VALID_ORCID_ID)).thenReturn(Optional.of(author));
            when(bookRepository.findAll()).thenReturn(booksList);
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            author.removeBook(book);
        }


        private void shouldReturnInvalidFieldMessageWhenPutAuthorBooks(final String URL, final String body, final String cause) throws Exception {
            mockMvc.perform(put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo(cause)));
        }


//    ============================================== PUT author's books ====================================================


        @Test
        void givenValidBookIsbnIdDtoList_whenUpdateAuthorBooks_thenReturnUpdatedAuthor() throws Exception {
            final List<BookIsbnIdDto> bookIsbnIdDtos = Arrays.asList(
                    new BookIsbnIdDto(newBook.getIsbnId())
            );

            mockMvc.perform(put("/authors/" + author.getOrcidId() + "/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(bookIsbnIdDtos)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orcidId", equalTo(updatedAuthor.getOrcidId())))
                    .andExpect(jsonPath("$.firstName", equalTo(updatedAuthor.getFirstName())))
                    .andExpect(jsonPath("$.lastName", equalTo(updatedAuthor.getLastName())))
                    .andExpect(jsonPath("$.books", hasSize(updatedAuthor.getBooks().size())))
                    .andExpect(jsonPath("$.books.[0].isbnId", equalTo(newBook.getIsbnId())))
                    .andExpect(jsonPath("$.books.[0].title", equalTo(newBook.getTitle())))
                    .andExpect(jsonPath("$.books.[0].publisher.id", equalTo(publisher.getId().intValue())))
                    .andExpect(jsonPath("$.books.[0].publisher.name", equalTo(publisher.getName())))
                    .andExpect(jsonPath("$.books.[0].publisher.address", equalTo(publisher.getAddress())))
                    .andExpect(jsonPath("$.books.[0].publisher.city", equalTo(publisher.getCity())))
                    .andExpect(jsonPath("$.books.[0].publisher.country", equalTo(publisher.getCountry())))
                    .andExpect(jsonPath("$.books.[0].publisher.books").doesNotExist());
        }


        @Test
        void givenShortIsbnIdInBookIsbnIdDtoList_whenUpdateAuthorBooks_thenReturnIsbnIdErrorMessage() throws Exception {
            final List<BookIsbnIdDto> bookIsbnIdDtos = Arrays.asList(
                    new BookIsbnIdDto(VALID_ISBN_ID.substring(VALID_ISBN_ID.length() - 1))
            );

            shouldReturnInvalidFieldMessageWhenPutAuthorBooks(
                    "/authors/" + author.getOrcidId() + "/books",
                    toJson(bookIsbnIdDtos),
                    "Book's ISBN code must only have digits of length of 13!"
            );
        }


        @Test
        void givenLongIsbnIdInBookIsbnIdDtoList_whenUpdateAuthorBooks_thenReturnIsbnIdErrorMessage() throws Exception {
            final List<BookIsbnIdDto> bookIsbnIdDtos = Arrays.asList(
                    new BookIsbnIdDto(VALID_ISBN_ID.repeat(2))
            );

            shouldReturnInvalidFieldMessageWhenPutAuthorBooks(
                    "/authors/" + author.getOrcidId() + "/books",
                    toJson(bookIsbnIdDtos),
                    "Book's ISBN code must only have digits of length of 13!"
            );
        }


        @Test
        void givenIsbnIdWithInvalidCharactersInBookIsbnIdDtoList_whenUpdateAuthorBooks_thenReturnIsbnIdErrorMessage() throws Exception {
            final List<BookIsbnIdDto> bookIsbnIdDtos = Arrays.asList(
                    new BookIsbnIdDto(VALID_ISBN_ID + "_A")
            );

            shouldReturnInvalidFieldMessageWhenPutAuthorBooks(
                    "/authors/" + author.getOrcidId() + "/books",
                    toJson(bookIsbnIdDtos),
                    "Book's ISBN code must only have digits of length of 13!"
            );
        }


        @Test
        void givenLongOrcidIdPathVariable_whenUpdateAuthorBooks_thenReturnOrcidIdErrorMessage() throws Exception {
            final List<BookIsbnIdDto> bookIsbnIdDtos = Arrays.asList(
                    new BookIsbnIdDto(newBook.getIsbnId())
            );

            shouldReturnInvalidFieldMessageWhenPutAuthorBooks(
                    "/authors/" + VALID_ORCID_ID.repeat(2) + "/books",
                    toJson(bookIsbnIdDtos),
                    "Author's ORCID code must only have digits of length of 16!"
            );
        }


        @Test
        void givenShortOrcidIdPathVariable_whenUpdateAuthorBooks_thenReturnOrcidIdErrorMessage() throws Exception {
            final List<BookIsbnIdDto> bookIsbnIdDtos = Arrays.asList(
                    new BookIsbnIdDto(newBook.getIsbnId())
            );

            shouldReturnInvalidFieldMessageWhenPutAuthorBooks(
                    "/authors/" + VALID_ORCID_ID.substring(VALID_ORCID_ID.length() - 1) + "/books",
                    toJson(bookIsbnIdDtos),
                    "Author's ORCID code must only have digits of length of 16!"
            );
        }


        @Test
        void givenOrcidIdWithInvalidCharactersPathVariable_whenUpdateAuthorBooks_thenReturnOrcidIdErrorMessage() throws Exception {
            final List<BookIsbnIdDto> bookIsbnIdDtos = Arrays.asList(
                    new BookIsbnIdDto(newBook.getIsbnId())
            );

            shouldReturnInvalidFieldMessageWhenPutAuthorBooks(
                    "/authors/" + VALID_ORCID_ID.replace(VALID_ORCID_ID.charAt(0), 'A') + "/books",
                    toJson(bookIsbnIdDtos),
                    "Author's ORCID code must only have digits of length of 16!"
            );
        }


        @Test
        void givenUnknownValidOrcidIdPathVariable_whenUpdateAuthorBooks_thenReturnOrcidIdErrorMessage() throws Exception {
            final List<BookIsbnIdDto> bookIsbnIdDtos = Arrays.asList(
                    new BookIsbnIdDto(newBook.getIsbnId())
            );

            final String unknownValidOrcidId = author.getOrcidId().replace(author.getOrcidId().charAt(0), (char) (author.getOrcidId().charAt(0) + 1));

            mockMvc.perform(put("/authors/" + unknownValidOrcidId + "/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(bookIsbnIdDtos)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.cause", equalTo("Author with ORCID code of " + unknownValidOrcidId + " not found!")));
        }


        @Test
        void givenInvalidJsonRequestBodies_whenUpdateAuthorBooks_thenReturnCauseMessages() throws Exception {
            final String unknownPropertyJson = "{ " +
                    "\"unknownProperty\": \"mockValue\"" +
                    "}";

            final Map<String, String> bodyCauseMap = Map.of(
                    "", cause.getIsEmptyOrInvalidObject(),
                    "null", cause.getIsEmptyOrInvalidObject(),
                    "{}", cause.getIsEmptyOrInvalidObject(),
                    "[]", cause.getIsEmptyOrInvalidObject(),
                    "[{}]", cause.getFieldIsNotInitialized(),
                    "{[]}", cause.getIsEmptyOrInvalidObject(),
                    "\n", cause.getJsonSyntaxError(),
                    unknownPropertyJson, cause.getIsEmptyOrInvalidObject()
            );

            for (final Map.Entry<String, String> entry : bodyCauseMap.entrySet()) {

                String body = entry.getKey();

                mockMvc.perform(put("/authors/" + VALID_ORCID_ID + "/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.cause", containsString(entry.getValue())));

            }

        }
    }


    private <T> String toJson(T authorAsDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(authorAsDto);
    }
}