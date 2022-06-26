package com.example.libraryprojectv2.domain.publisher.dto;

import lombok.Getter;

@Getter
public class PublisherDataDto extends PublisherIdDto {

    private final String name;
    private final String address;
    private final String city;
    private final String country;

    public PublisherDataDto(Long id, String name, String address, String city, String country) {
        super(id);
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
    }
}
