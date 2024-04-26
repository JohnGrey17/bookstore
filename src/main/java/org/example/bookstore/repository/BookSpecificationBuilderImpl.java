package org.example.bookstore.repository;

import org.example.bookstore.dto.BookSearchParameters;
import org.example.bookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilderImpl implements SpecificationBuilder<Book> {
    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        return null;
    }
}
