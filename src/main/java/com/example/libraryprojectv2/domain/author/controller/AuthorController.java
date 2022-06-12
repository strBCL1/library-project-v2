package com.example.libraryprojectv2.domain.author.controller;

import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/authors")
@Validated
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDataDto createAuthor(@RequestBody @NotNull @Valid final AuthorDataDto authorDataDto) {
        final AuthorDataDto savedAuthorDataDto = authorService.createAuthor(authorDataDto);
        return savedAuthorDataDto;
    }

    @GetMapping("/{orcid-id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDto getAuthorById(@PathVariable("orcid-id") @Pattern(regexp = "\\d{16}", message = "Author's ORCID code must only have digits of length of 16!") final String orcidId) {
        final AuthorDto authorDto = authorService.getAuthorByOrcidId(orcidId);
        return authorDto;
    }

}
