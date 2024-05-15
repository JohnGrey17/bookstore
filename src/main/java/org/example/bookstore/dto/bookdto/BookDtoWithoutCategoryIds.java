package org.example.bookstore.dto.bookdto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookDtoWithoutCategoryIds {
    private long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
