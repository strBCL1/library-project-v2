package com.example.libraryprojectv2.domain.publisher.model;

import com.example.libraryprojectv2.domain.book.model.Book;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "publisher")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publisher_generator")
    @SequenceGenerator(name = "publisher_generator", sequenceName = "publisher_id_seq", allocationSize = 1)
    @Positive
    private Long id;

    @Column(name = "name", length = 45)
    @Pattern(regexp = "[a-zA-Z]{0,45}", message = "Publisher's name may only contain up to 45 letters!")
    private String name;

    @Column(name = "address")
    @Size(max = 255, message = "Publisher's address may only contain up to 255 characters!")
    private String address;

    @Column(name = "city", length = 45)
    @Size(max = 45, message = "Publisher's city may only contain up to 45 characters!")
    private String city;

    @Column(name = "country", length = 45)
    @Size(max = 45, message = "Publisher's country may only contain up to 45 characters!")
    private String country;

    @Valid
    @OneToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY, mappedBy = "publisher")
    private Set<Book> books = new HashSet<>();

    public void updatePublisherData(final String name, final String address, final String city, final String country) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return id.equals(publisher.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
