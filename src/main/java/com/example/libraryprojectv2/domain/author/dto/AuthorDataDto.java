package com.example.libraryprojectv2.domain.author.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Getter
public class AuthorDataDto {

        @Pattern(regexp = "\\d{16}", message = "Author's ORCID code must only have digits of length of 16!")
        private final String orcidId;

        @Pattern(regexp = "\\w{1,45}", message = "Author's first name must have from 1 to 45 characters!")
        private final String firstName;

        @Pattern(regexp = "\\w{1,45}", message = "Author's last name must have from 1 to 45 characters!")
        private final String lastName;
}
