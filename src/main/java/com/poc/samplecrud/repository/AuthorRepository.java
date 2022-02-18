package com.poc.samplecrud.repository;

import com.poc.samplecrud.entity.AuthorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends MongoRepository<AuthorEntity, String> {
}
