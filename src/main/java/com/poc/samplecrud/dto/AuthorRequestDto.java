package com.poc.samplecrud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequestDto {
    private String authorName;
    private List<BookIsbnDto> books;
}
