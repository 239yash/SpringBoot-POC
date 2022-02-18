package com.poc.samplecrud.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "books")
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {
    private String bookName;
    private String author;
    private String publishedYear;
    @Id
    private String id;
    private String bookIsbn;
}
