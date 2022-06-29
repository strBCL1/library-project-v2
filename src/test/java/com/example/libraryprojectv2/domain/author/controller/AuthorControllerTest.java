package com.example.libraryprojectv2.domain.author.controller;

import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = AuthorControllerTestConfig.class)
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

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    Mapper mapper;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorService authorService;

    @Autowired
    AuthorController authorController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }


//    ============================================== GET author by ORCID ID ====================================================


    @Test
    void givenValidOrcidId_whenGetAuthorByOrcidId_thenReturnValidAuthor() throws Exception {
        final Author author = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
        final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
        final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

        author.addBook(book);
        book.updatePublisher(publisher);

        when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));

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

        verify(authorRepository, times(1)).findById(author.getOrcidId());
        verify(authorService, times(1)).getAuthorByOrcidId(author.getOrcidId());
        verify(authorController, times(1)).getAuthorByOrcidId(author.getOrcidId());
    }


    @Test
    void givenShortOrcidId_whenGetAuthorByOrcidId_thenReturnErrorMessage() throws Exception {
        final Author author = new Author(VALID_ORCID_ID.substring(VALID_ORCID_ID.length() - 1), VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
        final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
        final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

        author.addBook(book);
        book.updatePublisher(publisher);

        when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));

        mockMvc.perform(get("/authors/" + author.getOrcidId()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", equalTo("Author's ORCID code must only have digits of length of 16!")));

        verify(authorRepository, never()).findById(author.getOrcidId());
        verify(authorService, never()).getAuthorByOrcidId(author.getOrcidId());
    }


    @Test
    void givenLongOrcidId_whenGetAuthorByOrcidId_thenReturnErrorMessage() throws Exception {
        final Author author = new Author(VALID_ORCID_ID.repeat(2), VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
        final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
        final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

        author.addBook(book);
        book.updatePublisher(publisher);

        when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));

        mockMvc.perform(get("/authors/" + author.getOrcidId()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", equalTo("Author's ORCID code must only have digits of length of 16!")));

        verify(authorRepository, never()).findById(author.getOrcidId());
        verify(authorService, never()).getAuthorByOrcidId(author.getOrcidId());
    }


    @Test
    void givenOrcidIdWithLetters_whenGetAuthorByOrcidId_thenReturnErrorMessage() throws Exception {
        final Author author = new Author(VALID_ORCID_ID.replace(VALID_ORCID_ID.charAt(0), 'A'), VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
        final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
        final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

        author.addBook(book);
        book.updatePublisher(publisher);

        when(authorRepository.findById(anyString())).thenReturn(Optional.of(author));

        mockMvc.perform(get("/authors/" + author.getOrcidId()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", equalTo("Author's ORCID code must only have digits of length of 16!")));

        verify(authorRepository, never()).findById(author.getOrcidId());
        verify(authorService, never()).getAuthorByOrcidId(author.getOrcidId());
    }


//    ============================================== GET authors ====================================================


    @Test
    void givenAuthors_whenGetAuthors_thenReturnAuthors() throws Exception {
        final Author author1 = new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
        final Author author2 = new Author(VALID_ORCID_ID.replace(VALID_ORCID_ID.charAt(0), (char) (VALID_ORCID_ID.charAt(0) + 1)), VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>());
        final Book book = new Book(VALID_ISBN_ID, VALID_TITLE, new HashSet<>(), null);
        final Publisher publisher = new Publisher(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CITY, VALID_COUNTRY, new HashSet<>());

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


        verify(authorRepository, times(1)).findAll();
        verify(authorService, times(1)).getAuthors();
        verify(authorController, times(1)).getAuthors();
    }
}