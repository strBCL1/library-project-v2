package com.example.libraryprojectv2.domain.publisher.service;

import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import com.example.libraryprojectv2.domain.publisher.dao.PublisherRepository;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherDto;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final Mapper mapper;
    private final BookRepository bookRepository;


    public PublisherService(PublisherRepository publisherRepository, Mapper mapper, BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.mapper = mapper;
        this.bookRepository = bookRepository;
    }


    @Transactional
    public void deletePublisher(final long id) {
        if (!publisherRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    format("Publisher with id of {0} not found!", id)
            );
        }

//        Find books with given publisher's id
        final List<Book> books = bookRepository.findByPublisher_Id(id);

//        Remove found books' publisher
        books.forEach(book -> book.setPublisher(null));

//        Remove publisher from 'publisher' table
        publisherRepository.deleteById(id);
    }


    public List<PublisherDto> getPublishers() {
        final List<Publisher> publishers = publisherRepository.findAll();

        final List<PublisherDto> publisherDtos = publishers
                .stream()
                .map(mapper::publisherToPublisherDto)
                .toList();

        return publisherDtos;
    }


    public PublisherDto getPublisherById(final long id) {
        final Publisher publisher = getPublisherByIdOrThrowEntityNotFoundException(id);
        final PublisherDto publisherDto = mapper.publisherToPublisherDto(publisher);

        return publisherDto;
    }


    private Publisher getPublisherByIdOrThrowEntityNotFoundException(final long id) {
        return publisherRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Publisher with id of {0} not found!", id)
                ));
    }
}
