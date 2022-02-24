package com.poc.samplecrud.service;

import com.poc.samplecrud.dto.BookRequestDto;
import com.poc.samplecrud.dto.ResponseDto;

public interface BookOperationsService {
    ResponseDto getBooks();
    ResponseDto getBook(String id);
    ResponseDto createBook(BookRequestDto dto);
    ResponseDto deleteBook(String id);
    ResponseDto updateBook(String id, BookRequestDto dto);
}
