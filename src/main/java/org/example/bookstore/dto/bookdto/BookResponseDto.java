package org.example.bookstore.dto.bookdto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookResponseDto {
    private long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
