package org.example.bookstore.service.order;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.orderDto.OrderRequestDto;
import org.example.bookstore.dto.orderDto.OrderResponseDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.model.User;
import org.example.bookstore.model.order.Order;
import org.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.example.bookstore.repository.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    @Override
    public OrderResponseDto createNewOrder(OrderRequestDto requestDto,
                                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id
                        + "does not exist"));

        return null;
    }
}
