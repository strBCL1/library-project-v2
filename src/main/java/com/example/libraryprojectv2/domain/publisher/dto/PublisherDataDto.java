package com.example.libraryprojectv2.domain.publisher.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Getter
public class PublisherDataDto {

    @Pattern(regexp = "[a-zA-Z]{0,45}", message = "Publisher's name may only contain up to 45 letters!")
    private final String name;

    @Length(max = 255, message = "Publisher's address may only contain up to 255 characters!")
    private final String address;

    @Length(max = 45, message = "Publisher's city may only contain up to 45 characters!")
    private final String city;

    @Length(max = 45, message = "Publisher's country may only contain up to 45 characters!")
    private final String country;

    public PublisherDataDto(String name, String address, String city, String country) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
    }
}
