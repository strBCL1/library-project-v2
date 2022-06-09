package com.example.libraryprojectv2.domain.book.model;

import com.example.libraryprojectv2.domain.id.model.BaseIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "book")
public class Book extends BaseIdEntity {

    @Column(name = "title")
    private String title;

    public Book(final Long id, final String title) {
        super(id);
        this.title = title;
    }

    protected Book() {
        super();
    }
}
