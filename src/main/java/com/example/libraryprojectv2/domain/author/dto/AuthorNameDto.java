package com.example.libraryprojectv2.domain.author.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public record AuthorNameDto (
        @NotBlank @Length(max = 45)
        String firstName,

        @NotBlank @Length(max = 45)
        String lastName) {}
