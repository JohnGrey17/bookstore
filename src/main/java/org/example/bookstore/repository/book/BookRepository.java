package org.example.bookstore.repository.book;

import java.util.List;
import java.util.Optional;

import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>, JpaSpecificationExecutor<Book> {

    Optional<Book> findByIsbn(String isbn);
    Optional<BookResponseDto> findBookByCategoriesId(Long categories_id);
}
