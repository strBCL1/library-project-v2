package com.example.libraryprojectv2.domain.publisher.dao;

import com.example.libraryprojectv2.domain.publisher.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
