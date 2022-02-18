package com.poc.samplecrud.controller;

import com.poc.samplecrud.dto.AuthorRequestDto;
import com.poc.samplecrud.service.AuthorOperationsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorOperationsService authorOperationsService;

    @GetMapping({"/get/{id}", "/get"})
    public ResponseEntity<?> getAuthors(@PathVariable(required = false) String id) {
        return authorOperationsService.getAuthors(id);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable String id, @RequestBody AuthorRequestDto dto) {
        return authorOperationsService.updateAuthor(id, dto);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAuthor(@RequestBody AuthorRequestDto dto) {
        return authorOperationsService.createAuthor(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable String id) {
        return authorOperationsService.deleteAuthor(id);
    }
}
