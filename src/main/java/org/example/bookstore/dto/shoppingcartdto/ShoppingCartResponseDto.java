package org.example.bookstore.dto.shoppingcartdto;

import lombok.Data;
import org.example.bookstore.model.CartItem;

import java.util.Set;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItem> cartItems;
}
