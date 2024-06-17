package org.example.bookstore.service.shoppingcart;

import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cartitemdto.CartItemRequestDto;
import org.example.bookstore.dto.cartitemdto.CartItemUpdateDto;
import org.example.bookstore.dto.shoppingcartdto.ShoppingCartResponseDto;
import org.example.bookstore.dto.shoppingcartdto.ShoppingCartUpdatedDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.exception.ShoppingCartException;
import org.example.bookstore.mapper.ShoppingCartMapper;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.cartitem.CartItemRepository;
import org.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public void createNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookToCartItem(
            Long userId,
            CartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("can`t find shopping "
                        + "can`t by id: " + userId));
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(requestDto.getQuantity());
        cartItem.setBook(bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new DataIntegrityViolationException("Book "
                        + "not found for id: "
                        + requestDto.getBookId())));
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartResponseDto getUserShoppingCartById(Long cartId) {
        ShoppingCart shoppingCartByUserId = shoppingCartRepository.findShoppingCartById(cartId)
                .orElseThrow(() -> new ShoppingCartException("can`t find shoppingCart by id: "
                        + cartId));
        return shoppingCartMapper.toDto(shoppingCartByUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartResponseDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(userId).orElseThrow(
                        () -> new ShoppingCartException("You don`t have ShoppingCart"));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartUpdatedDto updateCartItemById(
            Long cartItemId,
            CartItemUpdateDto updateDto,
            Long userId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId).stream()
                .filter(c -> c.getShoppingCart().getUser().getId() == userId)
                .findFirst()
                .orElseThrow(() -> new ShoppingCartException(
                        "User don`t have cart item with id: " + cartItemId));

        cartItem.setQuantity(updateDto.getQuantity());
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toUpdatedCartItem(updateDto);
    }

    @Override
    @Transactional
    public void deleteCartById(
            Long cartItemId,
            Long userId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId).stream()
                .filter(c -> c.getShoppingCart().getUser().getId() == userId)
                .findFirst().orElseThrow(() -> new ShoppingCartException(
                        "User don`t have cart item with id: " + cartItemId));

        cartItemRepository.deleteById(cartItem.getId());
    }

    public ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart does not exist"));
    }
}
