package com.example.libraryprojectv2.domain.book.model;

import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import lombok.*;

import javax.persistence.*;
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
    private String isbnId;

    @Column(name = "title")
    private String title;

    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private Set<Author> authors = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Publisher publisher;

    public void updateBookData(final String title) {
        this.title = title;
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
