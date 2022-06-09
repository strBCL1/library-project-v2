package com.example.libraryprojectv2.domain.book.model;

import com.example.libraryprojectv2.domain.author.model.Author;
import com.example.libraryprojectv2.domain.id.model.BaseIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book extends BaseIdEntity {

    @Column(name = "title")
    private String title;

    @ManyToMany(mappedBy = "books")
    private Set<Author> authors = new HashSet<>();

    public Book(final String title, final Set<Author> authors) {
        this.title = title;
        this.authors = Set.copyOf(authors);
    }

    protected Book() {
        super();
    }

    public static final class BookBuilder {
        private String title;
        private Set<Author> authors = new HashSet<>();

        private BookBuilder() {
        }

        public static BookBuilder aBook() {
            return new BookBuilder();
        }

        public BookBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder authors(Set<Author> authors) {
            this.authors = authors;
            return this;
        }

        public Book build() {
            return new Book(title, authors);
        }
    }

    public String getTitle() {
        return title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }
}
