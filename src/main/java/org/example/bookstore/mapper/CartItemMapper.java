package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.cartitemdto.CartItemRequestDto;
import org.example.bookstore.dto.cartitemdto.CartItemResponseDto;
import org.example.bookstore.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);
    CartItem toModel(CartItemRequestDto requestDto);
}
