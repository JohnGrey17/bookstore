package org.example.bookstore.service.shoppingcart;

import org.example.bookstore.dto.cartitemdto.CartItemRequestDto;
import org.example.bookstore.dto.cartitemdto.CartItemUpdateDto;
import org.example.bookstore.dto.shoppingcartdto.ShoppingCartResponseDto;
import org.example.bookstore.dto.shoppingcartdto.ShoppingCartUpdatedDto;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;

public interface ShoppingCartService {
    void createNewShoppingCart(User user);

    ShoppingCartResponseDto addBookToCartItem(Long userId, CartItemRequestDto requestDto);

    ShoppingCartResponseDto getUserShoppingCartById(Long userId);

    ShoppingCartUpdatedDto updateCartItemById(Long cartItemId, CartItemUpdateDto updateDto);

    void deleteCartById(Long cartItemId);

    ShoppingCart getShoppingCartByUserId(Long userId);
}
