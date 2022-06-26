package com.example.libraryprojectv2.domain.author.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class AuthorIdDto extends AuthorDataDto {

//    org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot construct instance of ...
    private static final String DEFAULT_ORCID_ID = "0000000000000000";

    @Pattern(regexp = "\\d{16}", message = "Author's ORCID code must only have digits of length of 16!")
    private final String orcidId;

    public AuthorIdDto(String firstName, String lastName, String orcidId) {
        super(firstName, lastName);
        this.orcidId = orcidId;
    }

    public AuthorIdDto() {
        super();
        this.orcidId = DEFAULT_ORCID_ID;
    }
}
