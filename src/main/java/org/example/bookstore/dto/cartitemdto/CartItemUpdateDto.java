package org.example.bookstore.dto.cartitemdto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemUpdateDto {
    @Min(1)
    private Integer quantity;
}
