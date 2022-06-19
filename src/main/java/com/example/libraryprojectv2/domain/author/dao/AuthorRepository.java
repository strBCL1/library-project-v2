package com.example.libraryprojectv2.domain.author.dao;

import com.example.libraryprojectv2.domain.author.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

//    Find authors of book with given ISBN code
    List<Author> findByBooks_IsbnIdEquals(String isbnId);
}
