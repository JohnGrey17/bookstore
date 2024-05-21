package org.example.bookstore.dto.shoppingcartdto;

import lombok.Data;

@Data
public class ShoppingCartUpdatedDto {
    private String bookTitle;
    private Integer quantity;
}
