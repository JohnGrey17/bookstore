package org.example.bookstore.dto.shoppingcartdto;

import java.util.Set;
import lombok.Data;
import org.example.bookstore.dto.cartitemdto.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
