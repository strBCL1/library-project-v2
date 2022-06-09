package com.example.libraryprojectv2.domain.author.controller;

import com.example.libraryprojectv2.configuration.exception.InvalidInputException;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto createAuthor(@RequestBody @NotNull @Valid final AuthorDto authorDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidInputException("Wrong data input!");
        }

        final AuthorDto savedAuthorDto = authorService.createAuthor(authorDto);
        return savedAuthorDto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDto getAuthorById(@PathVariable @NotNull Long id) {
        final AuthorDto authorDto = authorService.getAuthorById(id);
        return authorDto;
    }

}
