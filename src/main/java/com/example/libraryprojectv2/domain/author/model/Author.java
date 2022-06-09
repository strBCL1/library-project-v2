package com.example.libraryprojectv2.domain.author.model;

import com.example.libraryprojectv2.domain.book.model.Book;
import com.example.libraryprojectv2.domain.id.model.BaseIdEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "author")
public class Author extends BaseIdEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    public Author(final Long id, final String firstName, final String lastName, final Set<Book> books) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.books = Set.copyOf(books);
    }

    protected Author() {
        super();
    }
}
