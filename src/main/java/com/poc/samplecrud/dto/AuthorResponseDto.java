package com.poc.samplecrud.dto;

import com.poc.samplecrud.entity.AuthorEntity;
import lombok.Data;
import java.util.List;

@Data
public class AuthorResponseDto {
    private String id;
    private String authorName;
    private List<BookIsbnDto> books;

    public AuthorResponseDto(AuthorEntity authorEntity) {
        this.id = authorEntity.getId();
        this.authorName = authorEntity.getAuthorName();
        this.books = authorEntity.getBooks();
    }
}
