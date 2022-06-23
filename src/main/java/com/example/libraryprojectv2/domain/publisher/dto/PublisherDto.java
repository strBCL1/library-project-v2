package com.example.libraryprojectv2.domain.publisher.dto;

import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
public class PublisherDto extends PublisherDataDto {

    @Valid
    private final Set<BookDataDto> books;

    public PublisherDto(Long id, String name, String address, String city, String country, Set<BookDataDto> books) {
        super(id, name, address, city, country);
        this.books = books;
    }
}
