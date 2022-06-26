package com.example.libraryprojectv2.domain.publisher.controller;

import com.example.libraryprojectv2.domain.publisher.dto.PublisherDto;
import com.example.libraryprojectv2.domain.publisher.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/publishers")
@Validated
public class PublisherController {


    private final PublisherService publisherService;


    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePublisher(@PathVariable @Pattern(regexp = "\\d+", message = "Publisher's id must only contain digits!") final String id) {
        publisherService.deletePublisher(Long.parseLong(id));
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PublisherDto> getPublishers() {
        final List<PublisherDto> publisherDtos = publisherService.getPublishers();
        return publisherDtos;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PublisherDto getPublisherById(@PathVariable @Pattern(regexp = "\\d+", message = "Publisher's id must only contain digits!") final String id) {
        final PublisherDto publisherDto = publisherService.getPublisherById(Long.parseLong(id));
        return publisherDto;
    }
}
