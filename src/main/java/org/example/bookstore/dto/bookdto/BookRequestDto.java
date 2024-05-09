package org.example.bookstore.dto.bookdto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @ISBN(type = ISBN.Type.ANY, message = "Illegal format")
    @Schema(description = "International Standard Book Number (ISBN) of the book",
            example = "978-3-16-148410-0", allowableValues = {"ISBN-10", "ISBN-13"})
    private String isbn;
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
