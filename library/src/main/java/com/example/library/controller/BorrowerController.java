package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.Borrower;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/borrowers")
public class BorrowerController {
    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;

    public BorrowerController(BorrowerRepository borrowerRepository,
                              BookRepository bookRepository) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("borrowers", borrowerRepository.findAll());
        return "borrowers/list";
    }

    @GetMapping("/new")
    public String getAddForm(Model model) {
        model.addAttribute("borrower", new Borrower());
        model.addAttribute("books", bookRepository.findAll());
        return "borrowers/form";
    }

    @PostMapping
    public String createBorrower(@Valid Borrower borrower, BindingResult bindingResult,
                                 @RequestParam(value = "selectedBooks", required = false) List<String> selectedBookIds) {
        if (bindingResult.hasErrors()) {
            return "borrowers/form";
        }
        Set<Book> selectedBooks = new HashSet<>();
        if (selectedBookIds != null) {
            for (String bookId : selectedBookIds) {
                Book book = bookRepository.findById(UUID.fromString(bookId)).orElse(null);
                if (book != null) {
                    selectedBooks.add(book);
                    book.getBorrowers().add(borrower);
                }
            }
        }

        borrower.setBooks(selectedBooks);
        borrowerRepository.save(borrower);

        return "redirect:/borrowers";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") UUID borrowerId, Model model) {
        Optional<Borrower> borrower = borrowerRepository.findById(borrowerId);
        if (borrower.isEmpty()) {
            return "not-found";
        }
        model.addAttribute("borrower", borrower.get());
        Set<Book> books = new HashSet<>();
        for (Book book : bookRepository.findAll()) {
            if (!book.getBorrowers().contains(borrower.get())) {
                books.add(book);
            }
        }

        model.addAttribute("books", books);
        return "borrowers/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") UUID borrowerId) {
        Optional<Borrower> borrower = borrowerRepository.findById(borrowerId);
        if (borrower.isEmpty()) {
            return "not-found";
        }
        borrowerRepository.delete(borrower.get());
        return "redirect:/borrowers";
    }

    @GetMapping("/return/{borrowerId}/{bookId}")
    public String returnBook(@PathVariable("borrowerId") UUID borrowerId,
                             @PathVariable("bookId") UUID bookId) {

        Optional<Borrower> borrowerOptional = borrowerRepository.findById(borrowerId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (borrowerOptional.isEmpty() && bookOptional.isEmpty()) {
            return "not-found";
        }
        Borrower borrower = borrowerOptional.get();
        Book book = bookOptional.get();
        borrower.getBooks().remove(book);
        book.getBorrowers().remove(borrower);
        borrowerRepository.save(borrower);
        bookRepository.save(book);
        return "redirect:/borrowers";
    }
}
