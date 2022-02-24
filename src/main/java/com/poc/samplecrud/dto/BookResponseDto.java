package com.poc.samplecrud.dto;

import com.poc.samplecrud.entity.BookEntity;
import lombok.Data;

@Data
public class BookResponseDto {
    private String bookName;
    private String author;
    private String publishedYear;
    private String id;
    private String bookIsbn;

    public BookResponseDto(BookEntity bookEntity) {
        this.bookName = bookEntity.getBookName();
        this.author = bookEntity.getAuthor();
        this.publishedYear = bookEntity.getPublishedYear();
        this.id = bookEntity.getId();
        this.bookIsbn = bookEntity.getBookIsbn();
    }
}
