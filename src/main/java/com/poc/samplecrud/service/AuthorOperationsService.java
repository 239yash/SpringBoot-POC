package com.poc.samplecrud.service;


import com.poc.samplecrud.dto.AuthorRequestDto;
import com.poc.samplecrud.dto.BookIsbnDto;
import com.poc.samplecrud.entity.AuthorEntity;
import com.poc.samplecrud.entity.BookEntity;
import com.poc.samplecrud.repository.AuthorRepository;
import com.poc.samplecrud.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorOperationsService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public ResponseEntity<?> getAuthors(String id) {
        if(Objects.isNull(id)) {
            try {
                List<?> authorList = authorRepository.findAll();
                return ResponseEntity.ok(authorList);
            } catch (Exception e) {
                log.error("Error Occurred");
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        try {
            Optional<AuthorEntity> authorEntity = authorRepository.findById(id);
            if(authorEntity.isPresent())
                return ResponseEntity.ok(authorEntity);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> createAuthor(AuthorRequestDto dto) {
        try {
            AuthorEntity authorEntity = AuthorEntity.builder()
                    .authorName(dto.getAuthorName())
                    .books(dto.getBooks())
                    .build();
            AuthorEntity _author = authorRepository.save(authorEntity);
            if(!Objects.isNull(_author.getBooks()))
            {
                List<BookIsbnDto> books = _author.getBooks();
                for(BookIsbnDto book : books) {
                    String bookId = book.getBookId();
                    try {
                        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
                        if(bookEntity.isEmpty()) {
                            BookEntity newBook = BookEntity.builder().author(_author.getId()).id(bookId).build();
                            bookRepository.save(newBook);
                        } else {
                            BookEntity _book = bookEntity.get();
                            _book.setAuthor(_author.getId());
                            bookRepository.save(_book);
                        }
                    } catch (Exception e) {
                        log.error("Error" + e);
                    }
                }
            }
            return new ResponseEntity<>(_author, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error Occurred", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteAuthor(String id) {
        try {
            authorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateAuthor(String id, AuthorRequestDto dto) {
        try {
            Optional<AuthorEntity> authorEntity = authorRepository.findById(id);
            if(authorEntity.isPresent()) {
                AuthorEntity _author = authorEntity.get();
                _author.setAuthorName(dto.getAuthorName());
                _author.setBooks(dto.getBooks());
                AuthorEntity author = authorRepository.save(_author);
                if(!Objects.isNull(author.getBooks())) {
                    List<BookIsbnDto> books = author.getBooks();
                    for (BookIsbnDto book : books) {
                        String bookId = book.getBookId();
                        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
                        BookEntity _book;
                        if (bookEntity.isEmpty()) {
                            _book = BookEntity.builder().author(id).id(bookId).build();
                        } else {
                            _book = bookEntity.get();
                           _book.setAuthor(id);
                        }
                        bookRepository.save(_book);
                    }
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
