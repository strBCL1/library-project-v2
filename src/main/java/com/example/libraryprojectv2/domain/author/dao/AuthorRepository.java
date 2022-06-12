package com.example.libraryprojectv2.domain.author.dao;

import com.example.libraryprojectv2.domain.author.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {
}
