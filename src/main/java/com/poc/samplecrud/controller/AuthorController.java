package com.poc.samplecrud.controller;

import com.poc.samplecrud.dto.AuthorRequestDto;
import com.poc.samplecrud.dto.ResponseDto;
import com.poc.samplecrud.service.AuthorOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private AuthorOperationsService authorOperationsService;
    private static final String ERROR = "ERROR";
    private static final String NOTFOUND = "NOT FOUND";

    @GetMapping("/")
    public ResponseEntity<ResponseDto> getAuthors() {
        ResponseDto dto = authorOperationsService.getAuthors();
        if(dto.getStatus().equals(ERROR))
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getAuthor(@PathVariable() String id) {
        ResponseDto dto = authorOperationsService.getAuthor(id);
        if(dto.getStatus().equals(ERROR))
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        if(dto.getStatus().equals(NOTFOUND))
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> updateAuthor(@PathVariable String id, @RequestBody AuthorRequestDto author) {
        ResponseDto dto = authorOperationsService.updateAuthor(id, author);
        if(dto.getStatus().equals(ERROR))
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        if(dto.getStatus().equals(NOTFOUND))
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseDto> createAuthor(@RequestBody AuthorRequestDto author) {
        ResponseDto dto = authorOperationsService.createAuthor(author);
        if(dto.getStatus().equals(ERROR))
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        if(dto.getStatus().equals(NOTFOUND))
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteAuthor(@PathVariable String id) {
        ResponseDto dto = authorOperationsService.deleteAuthor(id);
        if(dto.getStatus().equals(ERROR))
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        if(dto.getStatus().equals(NOTFOUND))
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
