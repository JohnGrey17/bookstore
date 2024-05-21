package org.example.bookstore.dto.cartitemdto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemUpdateDto {
    private String bookTitle;
    @Min(1)
    private Integer quantity;
}
