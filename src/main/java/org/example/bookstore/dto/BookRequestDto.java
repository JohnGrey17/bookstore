package org.example.bookstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Data;
import org.example.bookstore.validator.UniqueIsbn;
import org.hibernate.validator.constraints.ISBN;

@Data
public class BookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @ISBN(type = ISBN.Type.ANY, message = "Invalid format")
    @UniqueIsbn(message = "ISBN already exists")
    private String isbn;
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
