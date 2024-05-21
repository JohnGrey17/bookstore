package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.cartitemdto.CartItemResponseDto;
import org.example.bookstore.dto.shoppingcartdto.ShoppingCartResponseDto;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
//    @Mapping(target = "userId", source = "user", qualifiedByName = "getUserIdFromUser")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
//    @Named("getCartItemsDtoSet")
//    default Set<CartItemResponseDto> getCartItemsDtoSet(Set<CartItem> cartItemSet) {
//        return cartItemSet.stream()
//                .map(this::toCartItemResponseDto)
//                .collect(Collectors.toSet());
    }

//    @Named("getUserIdFromUser")
//    default Long getUserIdFromUser(User user) {
//        return user.getId();
//    }
//
//    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);

