package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.model.Author;
import com.example.LibraryManagementSystem.repository.AuthorRepository;
import com.example.LibraryManagementSystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        if (bookRepository.findAll().stream().anyMatch(book -> book.getAuthor().getId().equals(id))) {
            throw new RuntimeException("Cannot delete author connected to a book");
        }
        authorRepository.deleteById(id);
    }

    public List<Author> findByName(String name) {
        return authorRepository.findAll().stream()
                .filter(author -> author.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }
}