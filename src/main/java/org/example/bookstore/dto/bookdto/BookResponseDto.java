package org.example.bookstore.dto.bookdto;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import org.example.bookstore.model.Category;

@Data
public class BookResponseDto {
    private long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Category> categories;
}
