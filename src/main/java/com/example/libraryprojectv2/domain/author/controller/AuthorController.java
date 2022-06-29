package com.example.libraryprojectv2.domain.author.controller;

import com.example.libraryprojectv2.domain.author.dto.AuthorDataDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorDto;
import com.example.libraryprojectv2.domain.author.dto.AuthorIdDto;
import com.example.libraryprojectv2.domain.author.service.AuthorService;
import com.example.libraryprojectv2.domain.book.dto.BookIsbnIdDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

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
    public AuthorIdDto createAuthor(@RequestBody @NotNull @Valid final AuthorIdDto authorIdDto) {
        return authorService.createAuthor(authorIdDto);
    }


    @GetMapping("/{orcid-id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDto getAuthorByOrcidId(@PathVariable("orcid-id") @Pattern(regexp = "\\d{16}", message = "Author's ORCID code must only have digits of length of 16!") final String orcidId) {
        AuthorDto authorDto = authorService.getAuthorByOrcidId(orcidId);
        return authorDto;
    }


    @PutMapping("/{orcid-id}/books")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDto updateBooks(@RequestBody @NotNull @Valid final List<BookIsbnIdDto> bookIsbnIdDtos,
                                 @PathVariable("orcid-id") @Pattern(regexp = "\\d{16}", message = "Author's ORCID code must only have digits of length of 16!") final String orcidId) {
        return authorService.updateBooks(bookIsbnIdDtos, orcidId);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorDto> getAuthors() {
        return authorService.getAuthors();
    }


    @DeleteMapping("/{orcid-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable("orcid-id") @Pattern(regexp = "\\d{16}", message = "Author's ORCID code must only have digits of length of 16!") final String orcidId) {
        authorService.deleteAuthor(orcidId);
    }


    @PutMapping("/{orcid-id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDto updateAuthorData(@RequestBody @NotNull @Valid final AuthorDataDto authorDataDto,
                                          @PathVariable("orcid-id") @Pattern(regexp = "\\d{16}", message = "Author's ORCID code must only have digits of length of 16!") final String orcidId) {
        return authorService.updateAuthorData(authorDataDto, orcidId);
    }
}
