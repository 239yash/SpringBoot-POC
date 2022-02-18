package com.poc.samplecrud.controller;

import com.poc.samplecrud.dto.BookRequestDto;
import com.poc.samplecrud.service.BookOperationsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookOperationsService bookOperationsService;

    @GetMapping({"/get/{id}", "/get"})
    public ResponseEntity<?> getBooks(@PathVariable(required = false) String id) {
        return bookOperationsService.getBooks(id);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateBook(@PathVariable String id, @RequestBody BookRequestDto dto) {
        return bookOperationsService.updateBook(id, dto);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBook(@RequestBody BookRequestDto dto) {
        return bookOperationsService.createBook(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        return bookOperationsService.deleteBook(id);
    }
}
