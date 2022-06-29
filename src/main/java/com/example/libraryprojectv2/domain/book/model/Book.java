package com.example.libraryprojectv2.domain.book.model;

import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "authors")
public class Book {

    @Id
    @Column(name = "isbn_id", length = 13, updatable = false)
    @Pattern(regexp = "\\d{13}", message = "Book's ISBN code must only have digits of length of 13!")
    private String isbnId;

    @Column(name = "title")
    @Size(max = 255, message = "Book's title may only contain up to 255 characters!")
    private String title;

    @Valid
    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private Set<Author> authors = new HashSet<>();

    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    private Publisher publisher;

    public void updateBookData(final String title, final Publisher publisher) {
        this.title = title;
        updatePublisher(publisher);
    }

    public void updatePublisher(final Publisher publisher) {
        this.publisher = publisher;
        publisher.getBooks().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbnId.equals(book.isbnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbnId);
    }
}
