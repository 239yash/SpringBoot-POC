package com.poc.samplecrud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDto {
    private String bookName;
    @NonNull
    private String bookIsbn;
    private String author;
    private String publishedYear;
}
