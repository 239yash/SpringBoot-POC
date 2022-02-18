package com.poc.samplecrud.entity;

import com.poc.samplecrud.dto.BookIsbnDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "authors")
@AllArgsConstructor
@NoArgsConstructor
public class AuthorEntity {
    @Id
    private String id;
    private String authorName;
    private List<BookIsbnDto> books = new ArrayList<>();
}
