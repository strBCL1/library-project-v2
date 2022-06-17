package com.example.libraryprojectv2.domain.author.model;

import com.example.libraryprojectv2.domain.book.model.Book;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "author")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "books")
public class Author {

    @Id
    @Column(name = "orcid_id", length = 16)
    private String orcidId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "orcid_id"),
            inverseJoinColumns = @JoinColumn(name = "isbn_id")
    )
    private Set<Book> books = new HashSet<>();

    public void addBook(final Book book) {
        this.books.add(book);
        book.getAuthors().add(this);
    }

    public void updateAuthorData(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return orcidId.equals(author.orcidId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orcidId);
    }
}
