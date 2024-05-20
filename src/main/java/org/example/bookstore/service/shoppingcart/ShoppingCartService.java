package org.example.bookstore.service.shoppingcart;

import org.example.bookstore.dto.shoppingcartdto.ShoppingCartResponseDto;
import org.example.bookstore.model.User;

public interface ShoppingCartService {
void createNewShoppingCart(User user);
}
