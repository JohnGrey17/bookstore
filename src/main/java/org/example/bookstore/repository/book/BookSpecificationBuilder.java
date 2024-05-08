package org.example.bookstore.repository.book;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.bookdto.BookSearchParameters;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.SpecificationBuilder;
import org.example.bookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private static final String TITLE = "title";
    private static final String AUTHOR = "author";

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);
        specification = addSpecification(specification, searchParameters.author(), AUTHOR);
        specification = addSpecification(specification, searchParameters.title(),TITLE);

        return specification;
    }

    private Specification<Book> addSpecification(Specification<Book> specification,
                                                 String[] values, String field) {
        if (values != null && values.length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(field)
                    .getSpecification(values));
        }
        return specification;
    }
}
