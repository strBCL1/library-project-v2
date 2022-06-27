package com.example.libraryprojectv2.domain.publisher.dto;

import com.example.libraryprojectv2.domain.book.dto.BookFullInfoDto;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
public class PublisherDto extends PublisherIdDto {

    @Valid
    private final Set<BookFullInfoDto> books;

    public PublisherDto(String name, String address, String city, String country, Long id, Set<BookFullInfoDto> books) {
        super(name, address, city, country, id);
        this.books = books;
    }
}
