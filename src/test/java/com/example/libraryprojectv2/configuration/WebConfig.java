package com.example.libraryprojectv2.configuration;

import com.example.libraryprojectv2.configuration.response.handler.RestExceptionHandler;
import com.example.libraryprojectv2.domain.author.controller.AuthorController;
import com.example.libraryprojectv2.domain.author.dao.AuthorRepository;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@TestConfiguration
@EnableWebMvc
public class WebConfig {

    @Bean
    public Mapper mapper() {
        return Mockito.spy(Mapper.class);
    }

    @Bean
    public AuthorRepository authorRepository() {
        return Mockito.spy(AuthorRepository.class);
    }

    @Bean
    public BookRepository bookRepository() {
        return Mockito.spy(BookRepository.class);
    }

    @Bean
    public AuthorService authorService(@Qualifier("authorRepository") AuthorRepository authorRepository,
                                       @Qualifier("bookRepository") BookRepository bookRepository,
                                       @Qualifier("mapper") Mapper mapper) {
        return new AuthorService(authorRepository, bookRepository, mapper);
    }

    @Bean
    public AuthorController authorController(@Qualifier("authorService") AuthorService authorService) {
        return new AuthorController(authorService);
    }

    @Bean
    public RestExceptionHandler restExceptionHandler() {
        return Mockito.spy(RestExceptionHandler.class);
    }

//    @Bean
//    public LocalValidatorFactoryBean localValidatorFactoryBean() {
//        return new LocalValidatorFactoryBean();
//    }

    @Bean
    MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
