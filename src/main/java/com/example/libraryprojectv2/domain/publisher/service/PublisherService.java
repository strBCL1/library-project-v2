package com.example.libraryprojectv2.domain.publisher.service;

import com.example.libraryprojectv2.domain.book.dao.BookRepository;
import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.mapper.Mapper;
import com.example.libraryprojectv2.domain.publisher.dao.PublisherRepository;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherDataDto;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherDto;
import com.example.libraryprojectv2.domain.publisher.dto.PublisherIdDto;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;
    private final Mapper mapper;


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


    @Transactional
    public PublisherIdDto createPublisher(final PublisherDataDto publisherDataDto) {
        final Publisher newPublisher = mapper.publisherDataDtoToPublisher(publisherDataDto);
        final Long newPublisherId = newPublisher.getId();

        final Optional<Publisher> optionalPublisher = publisherRepository.findById(newPublisherId);

        if (optionalPublisher.isPresent()) {
            throw new EntityExistsException(
                    format("Publisher with ID of {0} already exists!", newPublisherId)
            );
        }

        final Publisher savedPublisher = publisherRepository.save(newPublisher);
        final PublisherIdDto savedAuthorIdDto = mapper.publisherToPublisherIdDto(savedPublisher);
        return savedAuthorIdDto;
    }


    @Transactional
    public PublisherDto updatePublisherData(final PublisherDataDto publisherDataDto, final long id) {
        final Publisher publisher = getPublisherByIdOrThrowEntityNotFoundException(id);

        publisher.updatePublisherData(
                publisherDataDto.getName(),
                publisherDataDto.getAddress(),
                publisherDataDto.getCity(),
                publisherDataDto.getCountry()
        );

        final Publisher savedPublisher = publisherRepository.save(publisher);
        final PublisherDto savedPublisherDto = mapper.publisherToPublisherDto(savedPublisher);
        return savedPublisherDto;
    }

    private Publisher getPublisherByIdOrThrowEntityNotFoundException(final long id) {
        return publisherRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Publisher with id of {0} not found!", id)
                ));
    }
}
