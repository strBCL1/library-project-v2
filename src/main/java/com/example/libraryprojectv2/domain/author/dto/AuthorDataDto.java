package com.example.libraryprojectv2.domain.author.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class AuthorDataDto {

        @Pattern(regexp = "[a-zA-Z]{0,45}", message = "Author's first name may only contain up to 45 letters!")
        private final String firstName;

        @Pattern(regexp = "[a-zA-Z]{0,45}", message = "Author's last name may only contain up to 45 letters!")
        private final String lastName;

        public AuthorDataDto(String firstName, String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
        }
}
