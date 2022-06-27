package com.example.libraryprojectv2.domain.publisher.dto;

import com.example.libraryprojectv2.domain.book.dto.BookDataDto;
import lombok.Getter;

import javax.validation.Valid;
import java.util.Set;

@Getter
public class PublisherDto extends PublisherIdDto {

    @Valid
    private final Set<BookDataDto> books;

    public PublisherDto(String name, String address, String city, String country, Long id, Set<BookDataDto> books) {
        super(name, address, city, country, id);
        this.books = books;
    }
}
