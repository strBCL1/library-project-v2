package com.example.libraryprojectv2.domain.author.controller;

import com.example.libraryprojectv2.configuration.response.handler.RestExceptionHandler;
import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@TestConfiguration
@EnableWebMvc
public class AuthorControllerTestConfig {

    @Bean
    public Mapper mapper() {
        return Mockito.spy(Mapper.INSTANCE);
    }

    @Bean
    public AuthorRepository authorRepository() {
        return Mockito.mock(AuthorRepository.class);
    }

    @Bean
    public BookRepository bookRepository() {
        return Mockito.mock(BookRepository.class);
    }

    @Bean
    public AuthorService authorService(@Qualifier("authorRepository") AuthorRepository authorRepository,
                                       @Qualifier("bookRepository") BookRepository bookRepository,
                                       @Qualifier("mapper") Mapper mapper) {
        return Mockito.spy(new AuthorService(authorRepository, bookRepository, mapper));
    }

    @Bean
    public AuthorController authorController(@Qualifier("authorService") AuthorService authorService) {
        return Mockito.spy(new AuthorController(authorService));
    }

    @Bean
    public RestExceptionHandler restExceptionHandler() {
        return Mockito.spy(new RestExceptionHandler());
    }

    @Bean
    MethodValidationPostProcessor methodValidationPostProcessor() {
        return Mockito.spy(new MethodValidationPostProcessor());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
