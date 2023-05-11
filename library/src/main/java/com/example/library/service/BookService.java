package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final GoogleBooksService googleBooksService;

    public BookService(BookRepository bookRepository, GoogleBooksService googleBooksService) {
        this.bookRepository = bookRepository;
        this.googleBooksService = googleBooksService;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(UUID id) {
        return bookRepository.findById(id);
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public void save(String title) throws JsonProcessingException {
        Book book = googleBooksService.fetchBookFromGoogle(title);
        if (book != null) {
            save(book);
        }
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }
}
