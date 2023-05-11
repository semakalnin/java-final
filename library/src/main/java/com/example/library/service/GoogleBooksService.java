package com.example.library.service;

import com.example.library.model.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleBooksService {
    private final RestTemplate restTemplate;

    public GoogleBooksService() {
        this.restTemplate = new RestTemplate();
    }

    private static final String URL = "https://www.googleapis.com/books/v1/volumes?q=intitle:%s&maxResults=3";

    public Book fetchBookFromGoogle(String title) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(String.format(URL, title), String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode itemsNode = root.get("items");
        if (itemsNode != null && itemsNode.isArray() && itemsNode.size() > 0) {
            JsonNode bookNode = itemsNode.get(0).get("volumeInfo");
            Book book = new Book();
            book.setTitle(bookNode.get("title").asText());
            JsonNode publisher = bookNode.get("publisher");
            if (publisher != null) {
                book.setPublisher(publisher.asText());
            }
            book.setAuthor(bookNode.get("authors").get(0).asText());
            book.setIsbn(bookNode.get("industryIdentifiers").get(0).get("identifier").asText());
            book.setImage(bookNode.get("imageLinks").get("thumbnail").asText());
            return book;
        }
        return null;
    }
}
