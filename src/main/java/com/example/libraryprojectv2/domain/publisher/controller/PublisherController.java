package com.example.libraryprojectv2.domain.publisher.controller;

import com.example.libraryprojectv2.domain.publisher.dto.PublisherDataDto;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherDto;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherIdDto;
import com.example.libraryprojectv2.domain.publisher.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
        return publisherService.getPublishers();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PublisherDto getPublisherById(@PathVariable @Pattern(regexp = "\\d+", message = "Publisher's id must only contain digits!") final String id) {
        return publisherService.getPublisherById(Long.parseLong(id));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherIdDto createPublisher(@RequestBody @NotNull @Valid final PublisherDataDto publisherDataDto) {
        return publisherService.createPublisher(publisherDataDto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PublisherDto updatePublisherData(@RequestBody @NotNull @Valid final PublisherDataDto publisherDataDto,
                                            @PathVariable @Pattern(regexp = "\\d+", message = "Publisher's id must only contain digits!") final String id) {
        return publisherService.updatePublisherData(publisherDataDto, Long.parseLong(id));
    }
}
