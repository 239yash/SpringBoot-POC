package com.poc.samplecrud.service;

import com.poc.samplecrud.dto.BookIsbnDto;
import com.poc.samplecrud.dto.BookRequestDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class BookOperationsService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public ResponseEntity<?> getBooks(String id) {
        if(Objects.isNull(id)) {
            try {
                List<?> bookList = bookRepository.findAll();
                return ResponseEntity.ok(bookList);
            } catch (Exception e) {
                log.error("Error Occurred");
                return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        try {
            Optional<BookEntity> bookEntity = bookRepository.findById(id);
            if(bookEntity.isPresent())
                return ResponseEntity.ok(bookEntity);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> createBook(BookRequestDto dto) {
        try {
            BookEntity bookEntity = BookEntity.builder()
                    .bookIsbn(dto.getBookIsbn())
                    .bookName(dto.getBookName())
                    .author(dto.getAuthor())
                    .publishedYear(dto.getPublishedYear())
                    .build();
            BookEntity _book = bookRepository.save(bookEntity);
            Optional<AuthorEntity> authorEntity = authorRepository.findById(dto.getAuthor());
            if(authorEntity.isPresent()) {
                AuthorEntity _author = authorEntity.get();
                List<BookIsbnDto> list = _author.getBooks();
                BookIsbnDto book = BookIsbnDto.builder().bookId(_book.getId()).build();
                list.add(book);
                _author.setBooks(list);
                AuthorEntity updatedAuthor = authorRepository.save(_author);
                log.info("Author Updated " + updatedAuthor);
            } else {
                List<BookIsbnDto> list = new ArrayList<>();
                BookIsbnDto book = BookIsbnDto.builder().bookId(_book.getId()).build();
                list.add(book);
                AuthorEntity authorEntity1 = AuthorEntity.builder().id(dto.getAuthor()).books(list).build();
                AuthorEntity addedAuthor = authorRepository.save(authorEntity1);
                log.info("Author Added " + addedAuthor);
            }
            log.info("Book Created " + _book);
            return new ResponseEntity<>(_book, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error Occurred", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteBook(String id) {
        try {
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateBook(String id, BookRequestDto dto) {
        try {
            Optional<BookEntity> bookEntity = bookRepository.findById(id);
            if(bookEntity.isPresent()) {
                Optional<AuthorEntity> authorEntity = authorRepository.findById(dto.getAuthor());
                if(authorEntity.isPresent()) {
                    AuthorEntity _author = authorEntity.get();
                    List<BookIsbnDto> list = _author.getBooks();
                    BookIsbnDto book = BookIsbnDto.builder().bookId(id).build();
                    list.add(book);
                    _author.setBooks(list);
                    AuthorEntity updatedAuthor = authorRepository.save(_author);
                    log.info("Author Updated " + updatedAuthor);
                } else {
                    List<BookIsbnDto> list = new ArrayList<>();
                    BookIsbnDto book = BookIsbnDto.builder().bookId(id).build();
                    list.add(book);
                    AuthorEntity authorEntity1 = AuthorEntity.builder().id(dto.getAuthor()).books(list).build();
                    AuthorEntity addedAuthor = authorRepository.save(authorEntity1);
                    log.info("Author Added " + addedAuthor);
                }
                BookEntity book = bookEntity.get();
                book.setBookName(dto.getBookName());
                book.setAuthor(dto.getAuthor());
                book.setPublishedYear(dto.getPublishedYear());
                BookEntity _book = bookRepository.save(book);
                return new ResponseEntity<>(_book, HttpStatus.OK);
            } else
                return new ResponseEntity<>("Not Found", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
