package com.example.libraryprojectv2.domain.author.controller;

import com.example.libraryprojectv2.configuration.response.handler.RestExceptionHandler;
import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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

    private final static Long VALID_ID = 1L;
    private final static String VALID_NAME = "name";
    private final static String VALID_ADDRESS = "address";
    private final static String VALID_CITY = "city";
    private final static String VALID_COUNTRY = "country";

    private final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
    private final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
    private final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

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

        when(authorRepository.findById(author.getOrcidId())).thenReturn(Optional.of(author));
    }

    @AfterEach
    void tearDown() {
        reset(authorRepository);
        reset(bookRepository);
    }


    @Nested
    class AuthorControllerGetTest {


        private void shouldReturnInvalidOrcidIdMessageWhenSendGetRequest(final String URL) throws Exception {
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
                    .andExpect(jsonPath("$.books", hasSize(1)))
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
            shouldReturnInvalidOrcidIdMessageWhenSendGetRequest("/authors/" +
                    VALID_ORCID_ID.substring(VALID_ORCID_ID.length() - 1));
        }


        @Test
        void givenLongOrcidId_whenGetAuthorByOrcidId_thenReturnOrcidIdErrorMessage() throws Exception {
            shouldReturnInvalidOrcidIdMessageWhenSendGetRequest("/authors/" +
                    VALID_ORCID_ID.repeat(2));
        }


        @Test
        void givenOrcidIdWithInvalidCharacters_whenGetAuthorByOrcidId_thenReturnOrcidIdErrorMessage() throws Exception {
            shouldReturnInvalidOrcidIdMessageWhenSendGetRequest("/authors/" +
                    VALID_ORCID_ID.replace(VALID_ORCID_ID.charAt(0), 'A'));
        }


        @Test
        void givenUnknownValidOrcidId_whenGetAuthorByOrcidId_thenReturnAuthorNotFoundErrorMessage() throws Exception {
            final String unknownValidOrcidId = author
                    .getOrcidId()
                    .replace(author.getOrcidId().charAt(0), (char) (author.getOrcidId().charAt(0) + 1));

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


//    ============================================== POST author ====================================================


        @Test
        void givenValidAuthorIdDto_whenCreateAuthor_thenReturnAuthorIdDto() throws Exception {
            final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID);
            final Author author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

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
            final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID.repeat(2));
            final Author author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            mockMvc.perform(post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorIdDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's ORCID code must only have digits of length of 16!', rejected value: '" + authorIdDto.getOrcidId() + "'")));
        }


        @Test
        void givenShortOrcidId_whenCreateAuthor_thenReturnOrcidIdErrorMessage() throws Exception {
            final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID.substring(VALID_ORCID_ID.length() - 1));
            final Author author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            mockMvc.perform(post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorIdDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's ORCID code must only have digits of length of 16!', rejected value: '" + authorIdDto.getOrcidId() + "'")));
        }


        @Test
        void givenOrcidIdWithInvalidCharacters_whenCreateAuthor_thenReturnOrcidIdErrorMessage() throws Exception {
            final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID.replace(VALID_ORCID_ID.charAt(0), 'A'));
            final Author author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            mockMvc.perform(post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorIdDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's ORCID code must only have digits of length of 16!', rejected value: '" + authorIdDto.getOrcidId() + "'")));
        }


        @Test
        void givenLongFirstName_whenCreateAuthor_thenReturnFirstNameErrorMessage() throws Exception {
            final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME.repeat(10), VALID_LAST_NAME, VALID_ORCID_ID);
            final Author author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            mockMvc.perform(post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorIdDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's first name may only contain up to 45 letters!', rejected value: '" + authorIdDto.getFirstName() + "'")));
        }


        @Test
        void givenFirstNameWithInvalidCharacters_whenCreateAuthor_thenReturnFirstNameErrorMessage() throws Exception {
            final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME + "_1", VALID_LAST_NAME, VALID_ORCID_ID);
            final Author author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            mockMvc.perform(post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorIdDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's first name may only contain up to 45 letters!', rejected value: '" + authorIdDto.getFirstName() + "'")));
        }


        @Test
        void givenLongLastName_whenCreateAuthor_thenReturnLastNameErrorMessage() throws Exception {
            final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME.repeat(10), VALID_ORCID_ID);
            final Author author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            mockMvc.perform(post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorIdDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's last name may only contain up to 45 letters!', rejected value: '" + authorIdDto.getLastName() + "'")));
        }


        @Test
        void givenLastNameWithInvalidCharacters_whenCreateAuthor_thenReturnLastNameErrorMessage() throws Exception {
            final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME + "_1", VALID_ORCID_ID);
            final Author author = new Author(authorIdDto.getOrcidId(), authorIdDto.getFirstName(), authorIdDto.getLastName(), Collections.emptySet());

            when(authorRepository.save(any(Author.class))).thenReturn(author);

            mockMvc.perform(post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorIdDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's last name may only contain up to 45 letters!', rejected value: '" + authorIdDto.getLastName() + "'")));
        }
    }


    @Nested
    class AuthorControllerPutTest {


//    ============================================== PUT author ====================================================


        @Test
        void givenValidAuthorDataDto_whenUpdateAuthorData_thenReturnUpdatedAuthorDto() throws Exception {
            final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
            final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
            final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

            author.addBook(book);
            book.updatePublisher(publisher);

            final int repeatTimes = 2;
            final AuthorDataDto authorDataDto = new AuthorDataDto(author.getFirstName().repeat(repeatTimes), author.getLastName().repeat(repeatTimes));
            final Author updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            mockMvc.perform(put("/authors/" + author.getOrcidId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orcidId", equalTo(author.getOrcidId())))
                    .andExpect(jsonPath("$.firstName", equalTo(author.getFirstName())))
                    .andExpect(jsonPath("$.lastName", equalTo(author.getLastName())))
                    .andExpect(jsonPath("$.books", hasSize(1)))
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
            final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
            final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
            final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

            author.addBook(book);
            book.updatePublisher(publisher);

            final int repeatTimes = 10;
            final AuthorDataDto authorDataDto = new AuthorDataDto(author.getFirstName().repeat(repeatTimes), author.getLastName());
            final Author updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            mockMvc.perform(put("/authors/" + author.getOrcidId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's first name may only contain up to 45 letters!', rejected value: '" + authorDataDto.getFirstName() + "'")));
        }


        @Test
        void givenFirstNameWithInvalidCharacters_whenUpdateAuthorData_thenReturnFirstNameErrorMessage() throws Exception {
            final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
            final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
            final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

            author.addBook(book);
            book.updatePublisher(publisher);

            final AuthorDataDto authorDataDto = new AuthorDataDto(author.getFirstName() + "_1", author.getLastName());
            final Author updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            mockMvc.perform(put("/authors/" + author.getOrcidId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's first name may only contain up to 45 letters!', rejected value: '" + authorDataDto.getFirstName() + "'")));
        }


        @Test
        void givenLongLastName_whenUpdateAuthorData_thenReturnLastNameErrorMessage() throws Exception {
            final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
            final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
            final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

            author.addBook(book);
            book.updatePublisher(publisher);

            final int repeatTimes = 10;
            final AuthorDataDto authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName().repeat(repeatTimes));
            final Author updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            mockMvc.perform(put("/authors/" + author.getOrcidId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's last name may only contain up to 45 letters!', rejected value: '" + authorDataDto.getLastName() + "'")));
        }


        @Test
        void givenLastNameWithInvalidCharacters_whenUpdateAuthorData_thenReturnLastNameErrorMessage() throws Exception {
            final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
            final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
            final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

            author.addBook(book);
            book.updatePublisher(publisher);

            final AuthorDataDto authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName() + "_1");
            final Author updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            mockMvc.perform(put("/authors/" + author.getOrcidId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's last name may only contain up to 45 letters!', rejected value: '" + authorDataDto.getLastName() + "'")));
        }


        @Test
        void givenLongOrcidId_whenUpdateAuthorData_thenReturnOrcidIdErrorMessage() throws Exception {
            final Author author = new Author(VALID_ORCID_ID.repeat(2), VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
            final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
            final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

            author.addBook(book);
            book.updatePublisher(publisher);

            final AuthorDataDto authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName());
            final Author updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            mockMvc.perform(put("/authors/" + author.getOrcidId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Author's ORCID code must only have digits of length of 16!")));
        }


        @Test
        void givenShortOrcidId_whenUpdateAuthorData_thenReturnOrcidIdErrorMessage() throws Exception {
            final Author author = new Author(VALID_ORCID_ID.substring(VALID_ORCID_ID.length() - 1), VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
            final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
            final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

            author.addBook(book);
            book.updatePublisher(publisher);

            final AuthorDataDto authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName());
            final Author updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            mockMvc.perform(put("/authors/" + author.getOrcidId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Author's ORCID code must only have digits of length of 16!")));
        }


        @Test
        void givenOrcidIdWithInvalidCharacters_whenUpdateAuthorData_thenReturnOrcidIdErrorMessage() throws Exception {
            final Author author = new Author(VALID_ORCID_ID.replace(VALID_ORCID_ID.charAt(0), 'A'), VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
            final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
            final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

            author.addBook(book);
            book.updatePublisher(publisher);

            final AuthorDataDto authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName());
            final Author updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            mockMvc.perform(put("/authors/" + author.getOrcidId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cause", equalTo("Author's ORCID code must only have digits of length of 16!")));
        }


        @Test
        void givenUnknownValidOrcidId_whenUpdateAuthorData_thenReturnAuthorNotFoundMessage() throws Exception {
            final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
            final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
            final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

            author.addBook(book);
            book.updatePublisher(publisher);

            final AuthorDataDto authorDataDto = new AuthorDataDto(author.getFirstName(), author.getLastName());
            final Author updatedAuthor = new Author(author.getOrcidId(), authorDataDto.getFirstName(), authorDataDto.getLastName(), author.getBooks());

            when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

            final String unknownValidOrcidId = author.getOrcidId().replace(author.getOrcidId().charAt(0), (char) (author.getOrcidId().charAt(0) + 1));

            mockMvc.perform(put("/authors/" + unknownValidOrcidId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(authorDataDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.cause", equalTo("Author with ORCID code of " + unknownValidOrcidId + " not found!")));
        }
    }


    private <T> String toJson(T authorAsDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(authorAsDto);
    }
}