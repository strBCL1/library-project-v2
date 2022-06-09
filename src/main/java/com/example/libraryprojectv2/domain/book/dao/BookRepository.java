package com.example.libraryprojectv2.domain.book.dao;

import com.example.libraryprojectv2.domain.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
