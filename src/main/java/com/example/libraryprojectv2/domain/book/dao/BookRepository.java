package com.example.libraryprojectv2.domain.book.dao;

import com.example.libraryprojectv2.domain.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByPublisher_Id(@NonNull Long id);
}
