package org.example.bookstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class BookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @ISBN(type = ISBN.Type.ANY,message = "Illegal format")
    private String isbn;
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
