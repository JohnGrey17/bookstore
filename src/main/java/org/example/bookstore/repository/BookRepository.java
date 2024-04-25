package org.example.bookstore.repository;

import org.example.bookstore.dto.BookDto;
import org.example.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {

}
