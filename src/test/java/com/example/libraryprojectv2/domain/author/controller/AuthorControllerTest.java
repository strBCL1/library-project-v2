package com.example.libraryprojectv2.domain.author.controller;

import com.example.libraryprojectv2.configuration.WebConfig;
import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.dto.BookIdDto;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherIdDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

@SpringJUnitWebConfig(classes = WebConfig.class)
class AuthorControllerTest {

    private final static String VALID_TITLE = "title";
    private final static PublisherIdDto VALID_PUBLISHER = new PublisherIdDto(
            "name", "address", "city", "country", 1L
    );
    private final static String VALID_ISBN_ID = "1111111111111";

    private final static String VALID_FIRST_NAME = "firstName";
    private final static String VALID_LAST_NAME = "lastName";
    private final static String VALID_ORCID_ID = "1111111111111111";
    private final static Set<BookIdDto> VALID_BOOKS = Set.of(
            new BookIdDto(VALID_TITLE, VALID_PUBLISHER, VALID_ISBN_ID)
    );

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
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

//        mapper = context.getBean(Mapper.class);
//        authorRepository = context.getBean(AuthorRepository.class);
//        bookRepository = context.getBean(BookRepository.class);
//        authorService = context.getBean(AuthorService.class);
//        authorController = context.getBean(AuthorController.class);
    }


//    ============================================== GET ====================================================


    @Test
    void givenValidOrcidId_whenGetAuthorByOrcidId_thenReturnValidAuthorDto() throws Exception {
        int a = 1;
    }
}