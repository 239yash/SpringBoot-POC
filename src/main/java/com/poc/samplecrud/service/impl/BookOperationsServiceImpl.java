package com.poc.samplecrud.service.impl;

import com.poc.samplecrud.dto.BookIsbnDto;
import com.poc.samplecrud.dto.BookRequestDto;
import com.poc.samplecrud.dto.BookResponseDto;
import com.poc.samplecrud.dto.ResponseDto;
import com.poc.samplecrud.entity.AuthorEntity;
import com.poc.samplecrud.entity.BookEntity;
import com.poc.samplecrud.repository.AuthorRepository;
import com.poc.samplecrud.repository.BookRepository;
import com.poc.samplecrud.service.BookOperationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class BookOperationsServiceImpl implements BookOperationsService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    private static final String ERROR = "ERROR";
    private static final String SUCCESS = "SUCCESS";
    private static final String CREATED = "BOOK CREATED";
    private static final String NOTFOUND = "NOT FOUND";

    @Override
    public ResponseDto getBooks() {
        List<BookResponseDto> list = new ArrayList<>();
        try {
            List<BookEntity> bookList = bookRepository.findAll();
            for(BookEntity book : bookList)
                list.add(new BookResponseDto(book));
            return new ResponseDto(SUCCESS, Optional.of(list));
        } catch (Exception e) {
            log.error("Error Occurred", e);
            return new ResponseDto(ERROR);
        }
    }

    @Override
    public ResponseDto getBook(String id) {
        try {
            Optional<BookEntity> bookEntity = bookRepository.findById(id);
            return bookEntity.map(entity -> new ResponseDto(SUCCESS, Optional.of(entity))).orElseGet(() -> new ResponseDto(NOTFOUND));
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseDto(ERROR);
        }
    }

    @Override
    public ResponseDto createBook(BookRequestDto dto) {
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
            return new ResponseDto(CREATED);
        } catch (Exception e) {
            log.error("Error Occurred", e);
            return new ResponseDto(ERROR);
        }
    }

    @Override
    public ResponseDto deleteBook(String id) {
        try {
            Optional<BookEntity> _book = bookRepository.findById(id);
            if(_book.isPresent()) {
                String authorId = _book.get().getAuthor();
                Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
                if(authorEntity.isPresent()) {
                    List<BookIsbnDto> list = authorEntity.get().getBooks();
                    list.removeIf(book -> book.getBookId().equals(id));
                    AuthorEntity _author = authorEntity.get();
                    _author.setBooks(list);
                    authorRepository.save(_author);
                }
                bookRepository.deleteById(id);
                return new ResponseDto(SUCCESS);
            } else return new ResponseDto(NOTFOUND);
        } catch (Exception e) {
            return new ResponseDto(ERROR);
        }
    }

    @Override
    public ResponseDto updateBook(String id, BookRequestDto dto) {
        try {
            Optional<BookEntity> bookEntity = bookRepository.findById(id);
            if(bookEntity.isPresent()) {
                Optional<AuthorEntity> authorEntity = authorRepository.findById(dto.getAuthor());
                if(authorEntity.isPresent()) {
                    AuthorEntity _author = authorEntity.get();
                    List<BookIsbnDto> list = _author.getBooks();
                    boolean flag = false;
                    for(BookIsbnDto book : list) {
                        if (book.getBookId().equals(id)) {
                            flag = true;
                            break;
                        }
                    }
                    if(!flag) {
                        BookIsbnDto book = BookIsbnDto.builder().bookId(id).build();
                        list.add(book);
                        _author.setBooks(list);
                        AuthorEntity updatedAuthor = authorRepository.save(_author);
                        log.info("Author Updated " + updatedAuthor);
                    }
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
                bookRepository.save(book);
                return new ResponseDto(SUCCESS);
            } else
                return new ResponseDto(NOTFOUND);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseDto(ERROR);
        }
    }
}
