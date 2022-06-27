package com.example.libraryprojectv2.domain.publisher.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
@JsonPropertyOrder({"id"})
public class PublisherIdDto extends PublisherDataDto {

    @Positive
    private final Long id;

    public PublisherIdDto(String name, String address, String city, String country, Long id) {
        super(name, address, city, country);
        this.id = id;
    }
}
