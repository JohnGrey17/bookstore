package org.example.bookstore.service.cartitem;

import org.example.bookstore.dto.cartitemdto.CartItemRequestDto;
import org.example.bookstore.dto.cartitemdto.CartItemResponseDto;
import org.example.bookstore.model.CartItem;
import org.springframework.stereotype.Service;

@Service
public interface CartItemService {
    CartItemResponseDto save(CartItemRequestDto cartItemRequestDto);

//    void updateCartItem(Long cartItemId, CartItemUpdateDto updateDto);

}
