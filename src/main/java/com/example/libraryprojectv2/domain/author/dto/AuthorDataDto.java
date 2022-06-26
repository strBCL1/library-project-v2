package com.example.libraryprojectv2.domain.author.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class AuthorDataDto {

//        org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot construct instance of ...
        private static final String DEFAULT_FIRST_NAME = "DEFAULT_FIRST_NAME";
        private static final String DEFAULT_LAST_NAME = "DEFAULT_LAST_NAME";

        @Pattern(regexp = "[a-zA-Z]{0,45}", message = "Author's first name may only contain up to 45 letters!")
        private final String firstName;

        @Pattern(regexp = "[a-zA-Z]{0,45}", message = "Author's last name may only contain up to 45 letters!")
        private final String lastName;

        public AuthorDataDto(String firstName, String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
        }

        protected AuthorDataDto() {
                this.firstName = DEFAULT_FIRST_NAME;
                this.lastName = DEFAULT_LAST_NAME;
        }
}
