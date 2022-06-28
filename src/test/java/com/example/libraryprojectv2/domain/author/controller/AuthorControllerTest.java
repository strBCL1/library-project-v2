package com.example.libraryprojectv2.domain.author.controller;

import com.example.libraryprojectv2.configuration.WebConfig;
import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.BookIdDto;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringJUnitWebConfig(classes = WebConfig.class)
class AuthorControllerTest {

    private final static String VALID_FIRST_NAME = "FIRSTNAME";
    private final static String VALID_LAST_NAME = "LASTNAME";
    private final static String VALID_ORCID_ID = "1111111111111111";
    private final static Set<BookIdDto> VALID_BOOKS = new HashSet<>();

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    Mapper mapper;

    AuthorRepository authorRepository;

    BookRepository bookRepository;

    AuthorService authorService;

    AuthorController authorController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        mapper = context.getBean(Mapper.class);
        authorRepository = context.getBean(AuthorRepository.class);
        bookRepository = context.getBean(BookRepository.class);
        authorService = context.getBean(AuthorService.class);
        authorController = context.getBean(AuthorController.class);
    }

    @Test
    void getAuthorByOrcidId() throws Exception {
        final AuthorDto authorDto = new AuthorDto(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_ORCID_ID, VALID_BOOKS);

        when(authorRepository.findById(anyString())).thenReturn(Optional.of(
                new Author(VALID_ORCID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, new HashSet<>())
        ));
        when(authorService.getAuthorByOrcidId(anyString())).thenReturn(authorDto);

//        mockMvc.perform(get("/authors/" + authorDto.getOrcidId()))
//                .andDo(print())
//                .andExpect(jsonPath("$.firstName", equalTo(authorDto.getFirstName())));

        mockMvc.perform(get("/authors/222"))
                .andDo(print())
                .andExpect(jsonPath("$.cause", equalTo(authorDto.getFirstName())));

        int a= 1;
    }
}