package com.poc.samplecrud.controller;

import com.poc.samplecrud.dto.BookRequestDto;
import com.poc.samplecrud.dto.ResponseDto;
import com.poc.samplecrud.service.BookOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookOperationsService bookOperationsService;
    private static final String ERROR = "ERROR";
    private static final String NOTFOUND = "NOT FOUND";

    @GetMapping("/")
    public ResponseEntity<ResponseDto> getBooks() {
        ResponseDto dto = bookOperationsService.getBooks();
        if(dto.getStatus().equals(ERROR))
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        if(dto.getStatus().equals(NOTFOUND))
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getBook(@PathVariable() String id) {
        ResponseDto dto = bookOperationsService.getBook(id);
        if(dto.getStatus().equals(ERROR))
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        if(dto.getStatus().equals(NOTFOUND))
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> updateBook(@PathVariable String id, @RequestBody BookRequestDto book) {
        ResponseDto dto = bookOperationsService.updateBook(id, book);
        if(dto.getStatus().equals(ERROR))
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        if(dto.getStatus().equals(NOTFOUND))
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseDto> createBook(@RequestBody BookRequestDto book) {
        ResponseDto dto = bookOperationsService.createBook(book);
        if(dto.getStatus().equals(ERROR))
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        if(dto.getStatus().equals(NOTFOUND))
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteBook(@PathVariable String id) {
        ResponseDto dto = bookOperationsService.deleteBook(id);
        if(dto.getStatus().equals(ERROR))
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        if(dto.getStatus().equals(NOTFOUND))
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
