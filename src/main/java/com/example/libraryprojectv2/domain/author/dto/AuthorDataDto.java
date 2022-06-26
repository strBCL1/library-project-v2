package com.example.libraryprojectv2.domain.author.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class AuthorDataDto {

//        org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot construct instance of ...
        private static final String DEFAULT_FIRST_NAME = "DEFAULT_FIRST_NAME";
        private static final String DEFAULT_LAST_NAME = "DEFAULT_LAST_NAME";

        @Pattern(regexp = "[a-zA-Z]{1,45}", message = "Author's first name must only have letters of length from 1 to 45!")
        private final String firstName;

        @Pattern(regexp = "[a-zA-Z]{1,45}", message = "Author's last name must only have letters of length from 1 to 45!")
        private final String lastName;

        public AuthorDataDto(String firstName, String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
        }

        public AuthorDataDto() {
                this.firstName = DEFAULT_FIRST_NAME;
                this.lastName = DEFAULT_LAST_NAME;
        }
}
