package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import com.example.library.service.GoogleBooksService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final GoogleBooksService googleBooksService;

    public BookController(BookService bookService, GoogleBooksService googleBooksService) {
        this.bookService = bookService;
        this.googleBooksService = googleBooksService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("books", bookService.findAll());

        return "books/list";
    }


    @PostMapping("/search-external")
    public String searchBookInGoogle(@RequestParam String title, Model model) {
        try {
            Book book = googleBooksService.fetchBookFromGoogle(title);
            model.addAttribute("book", book);
            return "books/add-book";

        } catch (JsonProcessingException e) {
            return "not-found";
        }
    }

    @PostMapping
    public String addBook(@Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/book";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String update(@PathVariable("id") UUID bookId, Model model) {
        Optional<Book> book = bookService.findById(bookId);
        if (book.isEmpty()) {
            return "not-found";
        }
        model.addAttribute("book", book.get());
        return "books/book";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        Optional<Book> book = bookService.findById(id);
        if (book.isEmpty()) {
            return "not-found";
        }
        bookService.delete(book.get());
        return "redirect:/books";
    }
}
