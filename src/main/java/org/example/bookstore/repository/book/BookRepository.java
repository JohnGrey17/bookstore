package org.example.bookstore.repository.book;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>, JpaSpecificationExecutor<Book> {

    @Query("SELECT b FROM Book b JOIN FETCH b.categories")
    List<Book> findAllWithCategories();

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT b FROM Book b JOIN FETCH b.categories WHERE b.id = :bookId")
    Optional<Book> findByIdWithCategories(Long bookId);

    @Query("FROM Book b JOIN FETCH b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(Long categoryId, Pageable pageable);
}
