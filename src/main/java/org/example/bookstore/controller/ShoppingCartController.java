package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cartitemdto.CartItemRequestDto;
import org.example.bookstore.dto.cartitemdto.CartItemUpdateDto;
import org.example.bookstore.dto.shoppingcartdto.ShoppingCartResponseDto;
import org.example.bookstore.dto.shoppingcartdto.ShoppingCartUpdatedDto;
import org.example.bookstore.model.User;
import org.example.bookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart", description = "Operations related to shopping cart management")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Add Book to Shopping Cart", description = "Add a book to the user's "
            + "shopping cart. Only authorized user and admin can do that ")
    public ShoppingCartResponseDto addBookToShoppingCart(
            Authentication authentication,
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBookToCartItem(user.getId(), cartItemRequestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get Shopping Cart", description = "Get all shopping cart content "
            + "of user")
    public ShoppingCartResponseDto getShoppingCartInfo(
            @AuthenticationPrincipal User user) {
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @GetMapping("/{cartId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get User's Shopping Cart", description = "Retrieve the user's "
            + "shopping cart by user ID.")
    public ShoppingCartResponseDto getUserShoppingCartById(
            @PathVariable Long cartId) {
        return shoppingCartService.getUserShoppingCartById(cartId);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Update Cart Item", description =
            "Update a quantity in cartItem with " + "the given ID.")
    public ShoppingCartUpdatedDto updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemUpdateDto updateDto,
            @AuthenticationPrincipal User user) {
        return shoppingCartService.updateCartItemById(cartItemId, updateDto, user.getId());
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Delete Cart Item", description = "Delete a cart item "
            + "with the given ID.")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCartItemById(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal User user) {
        shoppingCartService.deleteCartById(cartItemId, user.getId());
    }
}
