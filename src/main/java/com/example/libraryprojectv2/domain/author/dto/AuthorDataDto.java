package com.example.libraryprojectv2.domain.author.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class AuthorDataDto {

        @Pattern(regexp = "[a-zA-Z]{1,45}", message = "Author's first name must have from 1 to 45 characters!")
        private final String firstName;

        @Pattern(regexp = "[a-zA-Z]{1,45}", message = "Author's last name must have from 1 to 45 characters!")
        private final String lastName;

        public AuthorDataDto(String firstName, String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
        }
}
