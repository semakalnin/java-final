package com.example.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "title", nullable = false, length = 100)
    @NotBlank(message = "must not be empty")
    @Size(min = 1, max = 50, message = "must be 1-50 character length")
    private String title;
    @Column(name = "publisher", length = 100)
    private String publisher;
    @Column(name = "isbn", nullable = false, length = 30)
    @NotEmpty(message = "must not be empty")
    private String isbn;
    @Column(name = "author", nullable = false, length = 100)
    @NotEmpty(message = "must not be empty")
    private String author;

    @Column(name = "cover")
    private String image;


    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "borrower_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "borrower_id")
    )
    private Set<Borrower> borrowers = new HashSet<>();

    public void addBorrower(Borrower borrower) {
        this.borrowers.add(borrower);
        borrower.getBooks().add(this);
    }

    public void removeBorrower(UUID borrowerId) {
        Borrower borrower = this.borrowers.stream().filter(b -> b.getId() == borrowerId).findFirst().orElse(null);
        if (borrower != null) {
            this.borrowers.remove(borrower);
            borrower.getBooks().remove(this);
        }
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String authors) {
        this.author = authors;
    }

    public Set<Borrower> getBorrowers() {
        return borrowers;
    }

    public void setBorrowers(Set<Borrower> borrowers) {
        this.borrowers = borrowers;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", title, isbn, author, publisher, image);
    }
}
