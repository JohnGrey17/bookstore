package org.example.bookstore.repository;

import java.util.List;
import org.example.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
    Book save(Book book);

    List<Book> findAll();

    Book getBookById(Long id);
}
