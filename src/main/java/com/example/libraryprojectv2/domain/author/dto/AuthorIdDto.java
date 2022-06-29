package com.example.libraryprojectv2.domain.author.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
@JsonPropertyOrder({"orcidId"})
public class AuthorIdDto extends AuthorDataDto {

    private static final String DEFAULT_ORCID_ID = "0000000000000000";

    @Pattern(regexp = "\\d{16}", message = "Author's ORCID code must only have digits of length of 16!")
    private final String orcidId;

    public AuthorIdDto(String firstName, String lastName, String orcidId) {
        super(firstName, lastName);
        this.orcidId = orcidId;
    }

    protected AuthorIdDto() {
        super();
        this.orcidId = DEFAULT_ORCID_ID;
    }
}
