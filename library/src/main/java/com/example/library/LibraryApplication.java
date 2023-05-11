package com.example.library;

import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.service.BookService;
import com.example.library.service.GoogleBooksService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,
                                        PasswordEncoder passwordEncoder,
                                        BookService bookService,
                                        GoogleBooksService googleBooksService) {
        return args -> {
            userRepository.save(new User("john", passwordEncoder.encode("password")));
            userRepository.save(new User("jake", passwordEncoder.encode("123qwer")));


            bookService.save("thinking in java");
            bookService.save("war and piece");
            bookService.save("shoot");
            bookService.save("c++");
            bookService.save("one day");
            bookService.save("first to arrive");
            bookService.save("legends");
            bookService.save("roman");
            bookService.save("bait");
        };
    }

}
