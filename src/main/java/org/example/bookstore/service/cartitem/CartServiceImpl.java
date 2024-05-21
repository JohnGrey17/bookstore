package org.example.bookstore.service.cartitem;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cartitemdto.CartItemRequestDto;
import org.example.bookstore.dto.cartitemdto.CartItemResponseDto;
import org.example.bookstore.mapper.CartItemMapper;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.repository.cartItem.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    @Override
    public CartItemResponseDto save(CartItemRequestDto cartItemRequestDto) {
        CartItem item = cartItemMapper.toModel(cartItemRequestDto);
        return cartItemMapper.toDto(cartItemRepository.save(item));
    }
}
