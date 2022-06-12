package com.example.libraryprojectv2.domain.book.model;

import com.example.libraryprojectv2.domain.author.model.Author;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Book {

    @Id
    @Column(name = "isbn_id", length = 13)
    private String isbnId;

    @Column(name = "title")
    private String title;

    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private Set<Author> authors = new HashSet<>();
}
