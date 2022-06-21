package com.example.libraryprojectv2.domain.author.controller;

import com.example.libraryprojectv2.configuration.response.handler.RestExceptionHandler;
import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthorControllerTest {

    private final static String VALID_FIRST_NAME = "firstName";
    private final static String VALID_LAST_NAME = "lastName";
    private final static String VALID_ORCID_ID = "0000000000000000";
    private final static String VALID_TITLE = "title";
    private final static String VALID_ISBN_ID = "0000000000000";
    private final static Set<BookDataDto> VALID_BOOK_SET = Set.of(
            new BookDataDto(VALID_ISBN_ID, VALID_TITLE),
            new BookDataDto(VALID_ISBN_ID, VALID_TITLE)
    );

    @InjectMocks
    AuthorController authorController;

    @Mock
    AuthorService authorService;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    Mapper mapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(authorController)
                .setControllerAdvice(RestExceptionHandler.class)
                .build();
    }


//        ========================================== POST ===============================================


//    Correct POST
    @Test
    void givenValidAuthorIdDto_whenCreateAuthor_thenReturnCreatedAuthorIdDto() throws Exception {
        final AuthorIdDto authorIdDto = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID);

        final AuthorIdDto createdAuthorIdDto = new AuthorIdDto(authorIdDto.getFirstName(), authorIdDto.getLastName(), authorIdDto.getOrcidId());

        when(authorService.createAuthor(any(AuthorIdDto.class))).thenReturn(createdAuthorIdDto);

        mockMvc.perform(post("/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(authorIdDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orcidId", equalTo(authorIdDto.getOrcidId())))
                .andExpect(jsonPath("$.firstName", equalTo(authorIdDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(authorIdDto.getLastName())));
    }

//    Incorrect POST - orcidId
    @Test
    void givenInValidAuthorIdDto_whenCreateAuthor_thenExpectConstraintViolationExceptionOfOrcidId() throws Exception {
        final AuthorIdDto onlyDigitsOrcidId = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, "111111111111");
        final AuthorIdDto createdOnlyDigitsOrcidId = new AuthorIdDto(onlyDigitsOrcidId.getFirstName(), onlyDigitsOrcidId.getLastName(), onlyDigitsOrcidId.getOrcidId());

        when(authorService.createAuthor(any(AuthorIdDto.class))).thenReturn(createdOnlyDigitsOrcidId);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(onlyDigitsOrcidId)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's ORCID code must only have digits of length of 16!', submitted value: '" + onlyDigitsOrcidId.getOrcidId() + "'")));

        final AuthorIdDto onlyLettersOrcidId = new AuthorIdDto(VALID_FIRST_NAME, VALID_LAST_NAME, "asdfghjklqwer");
        final AuthorIdDto createdOnlyLettersOrcidId = new AuthorIdDto(onlyLettersOrcidId.getFirstName(), onlyLettersOrcidId.getLastName(), onlyLettersOrcidId.getOrcidId());

        when(authorService.createAuthor(any(AuthorIdDto.class))).thenReturn(createdOnlyLettersOrcidId);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(onlyLettersOrcidId)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's ORCID code must only have digits of length of 16!', submitted value: '" + onlyLettersOrcidId.getOrcidId() + "'")));
    }

//    Incorrect POST - firstName
    @Test
    void givenInValidAuthorIdDto_whenCreateAuthor_thenExpectConstraintViolationExceptionOfFirstName() throws Exception {
        final AuthorIdDto longFirstName = new AuthorIdDto("jklafdlkafkjfklajsdkfjasfjkdlasafjsdklasjfakls", VALID_LAST_NAME, VALID_ORCID_ID);
        final AuthorIdDto createdLongFirstName = new AuthorIdDto(longFirstName.getFirstName(), longFirstName.getLastName(), longFirstName.getOrcidId());

        when(authorService.createAuthor(any(AuthorIdDto.class))).thenReturn(createdLongFirstName);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(longFirstName)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's first name must only have letters of length from 1 to 45!', submitted value: '" + longFirstName.getFirstName() + "'")));

        final AuthorIdDto digitsInFirstName = new AuthorIdDto("nice_fir5tname", VALID_LAST_NAME, VALID_ORCID_ID);
        final AuthorIdDto createdDigitsInFirstName = new AuthorIdDto(digitsInFirstName.getFirstName(), digitsInFirstName.getLastName(), digitsInFirstName.getOrcidId());

        when(authorService.createAuthor(any(AuthorIdDto.class))).thenReturn(createdDigitsInFirstName);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(digitsInFirstName)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's first name must only have letters of length from 1 to 45!', submitted value: '" + digitsInFirstName.getFirstName() + "'")));

    }

//    Incorrect POST - lastName
    @Test
    void givenInValidAuthorIdDto_whenCreateAuthor_thenExpectConstraintViolationExceptionOfLastName() throws Exception {
        final AuthorIdDto longLastName = new AuthorIdDto(VALID_FIRST_NAME, "jklafdlkafkjfklajsdkfjasfjkdlasafjsdklasjfakls", VALID_ORCID_ID);
        final AuthorIdDto createdLongLastName = new AuthorIdDto(longLastName.getFirstName(), longLastName.getLastName(), longLastName.getOrcidId());

        when(authorService.createAuthor(any(AuthorIdDto.class))).thenReturn(createdLongLastName);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(longLastName)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's last name must only have letters of length from 1 to 45!', submitted value: '" + longLastName.getLastName() + "'")));

        final AuthorIdDto digitsInLastName = new AuthorIdDto(VALID_FIRST_NAME, "nice_la5tname", VALID_ORCID_ID);
        final AuthorIdDto createdDigitsInLastName = new AuthorIdDto(digitsInLastName.getFirstName(), digitsInLastName.getLastName(), digitsInLastName.getOrcidId());

        when(authorService.createAuthor(any(AuthorIdDto.class))).thenReturn(createdDigitsInLastName);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(digitsInLastName)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", equalTo("Message: 'Author's last name must only have letters of length from 1 to 45!', submitted value: '" + digitsInLastName.getLastName() + "'")));

    }


//        ========================================== GET ===============================================


//    Correct GET by orcidId
    @Test
    void givenValidAuthorDto_whenGetAuthorById_thenReturnValidAuthorDto() throws Exception {
        final AuthorDto authorDto = new AuthorDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID, VALID_BOOK_SET);

        when(authorService.getAuthorByOrcidId(any(String.class))).thenReturn(authorDto);

        mockMvc.perform(get("/authors/" + authorDto.getOrcidId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(authorDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(authorDto.getLastName())))
                .andExpect(jsonPath("$.orcidId", equalTo(authorDto.getOrcidId())))
                .andExpect(jsonPath("$.books", hasSize(authorDto.getBooks().size())));
    }

//    Incorrect GET by orcidId - short and long orcidId
    @Test
    void givenValidAuthorDto_whenGetAuthorById_thenExpectConstraintViolationExceptionOfOrcidId() throws Exception {
        final AuthorDto authorDto = new AuthorDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID, VALID_BOOK_SET);

        when(authorService.getAuthorByOrcidId(any(String.class))).thenReturn(authorDto);

        mockMvc.perform(get("/authors/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", equalTo("Author's ORCID code must only have digits of length of 16!")));

    }

    @Test
    void getAuthorById() {
    }

    @Test
    void updateBooksOfAuthor() {
    }

    @Test
    void getAuthors() {
    }

    @Test
    void deleteAuthor() {
    }

    @Test
    void updateAuthorData() {
    }

    private <T> String toJson(T authorAsDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(authorAsDto);
    }
}