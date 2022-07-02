package com.example.libraryprojectv2.domain.author.model;

import com.example.libraryprojectv2.domain.book.model.Book;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
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
    @Column(name = "orcid_id", length = 16, updatable = false)
    @Pattern(regexp = "\\d{16}", message = "Author's ORCID code must only have digits of length of 16!")
    private String orcidId;

    @Column(name = "first_name", length = 45)
    @Pattern(regexp = "[a-zA-Z]{0,45}", message = "Author's first name may only contain up to 45 letters!")
    private String firstName;

    @Column(name = "last_name", length = 45)
    @Pattern(regexp = "[a-zA-Z]{0,45}", message = "Author's last name may only contain up to 45 letters!")
    private String lastName;

    @Valid
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

    public void removeBook(final Book book) {
        this.books.remove(book);
        book.getAuthors().remove(this);
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
