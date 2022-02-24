package com.poc.samplecrud.service.impl;


import com.poc.samplecrud.dto.AuthorRequestDto;
import com.poc.samplecrud.dto.AuthorResponseDto;
import com.poc.samplecrud.dto.BookIsbnDto;
import com.poc.samplecrud.dto.ResponseDto;
import com.poc.samplecrud.entity.AuthorEntity;
import com.poc.samplecrud.entity.BookEntity;
import com.poc.samplecrud.repository.AuthorRepository;
import com.poc.samplecrud.repository.BookRepository;
import com.poc.samplecrud.service.AuthorOperationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AuthorOperationsServiceImpl implements AuthorOperationsService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    private static final String ERROR = "ERROR";
    private static final String SUCCESS = "SUCCESS";
    private static final String CREATED = "AUTHOR CREATED";
    private static final String NOTFOUND = "NOT FOUND";

    @Override
    public ResponseDto getAuthors() {
        List<AuthorResponseDto> list = new ArrayList<>();
        try {
            List<AuthorEntity> authorList = authorRepository.findAll();
            for(AuthorEntity author : authorList)
                list.add(new AuthorResponseDto(author));
            return new ResponseDto(SUCCESS, Optional.of(list));
        } catch (Exception e) {
            log.error("Error Occurred");
            return new ResponseDto(ERROR);
        }
    }

    @Override
    public ResponseDto getAuthor(String id) {
        try {
            Optional<AuthorEntity> authorEntity = authorRepository.findById(id);
            if(authorEntity.isPresent()) {
                AuthorResponseDto author = new AuthorResponseDto(authorEntity.get());
                return new ResponseDto(SUCCESS, Optional.of(author));
            }
            else return new ResponseDto(NOTFOUND);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseDto(ERROR);
        }
    }

    @Override
    public ResponseDto createAuthor(AuthorRequestDto dto) {
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
                            String authorId = _book.getAuthor();
                            try {
                                Optional<AuthorEntity> author = authorRepository.findById(authorId);
                                if(author.isPresent()) {
                                    List<BookIsbnDto> list = author.get().getBooks();
                                    list.removeIf(b -> b.getBookId().equals(_book.getId()));
                                    AuthorEntity updatedAuthor = author.get();
                                    updatedAuthor.setBooks(list);
                                    authorRepository.save(updatedAuthor);
                                }
                            } catch (Exception e) {
                                log.error("Error", e);
                            }
                            _book.setAuthor(_author.getId());
                            bookRepository.save(_book);
                        }
                    } catch (Exception e) {
                        log.error("Error" + e);
                    }
                }
            }
            return new ResponseDto(CREATED);
        } catch (Exception e) {
            log.error("Error Occurred", e);
            return new ResponseDto(ERROR);
        }
    }

    @Override
    public ResponseDto deleteAuthor(String id) {
        try {
            authorRepository.deleteById(id);
            return new ResponseDto(SUCCESS);
        } catch (Exception e) {
            return new ResponseDto(ERROR);
        }
    }

    @Override
    public ResponseDto updateAuthor(String id, AuthorRequestDto dto) {
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
                        try {
                            Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
                            BookEntity _book;
                            if (bookEntity.isEmpty()) {
                                _book = BookEntity.builder().author(id).id(bookId).build();
                            } else {
                                _book = bookEntity.get();
                                _book.setAuthor(id);
                            }
                            bookRepository.save(_book);
                        } catch (Exception e) {
                            log.error("Error", e);
                        }
                    }
                }
                return new ResponseDto(SUCCESS);
            } else {
                return new ResponseDto(NOTFOUND);
            }
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseDto(ERROR);
        }
    }
}
