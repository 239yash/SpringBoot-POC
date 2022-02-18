package com.poc.samplecrud.repository;

import com.poc.samplecrud.entity.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<BookEntity, String> {
}
