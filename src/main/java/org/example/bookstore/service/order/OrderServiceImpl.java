package org.example.bookstore.service.order;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.orderDto.OrderRequestDto;
import org.example.bookstore.dto.orderDto.OrderResponseDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.OrderItemsMapper;
import org.example.bookstore.mapper.OrderMapper;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.OrderItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.model.order.Order;
import org.example.bookstore.model.status.Status;
import org.example.bookstore.repository.cartitem.CartItemRepository;
import org.example.bookstore.repository.order.OrderRepository;
import org.example.bookstore.repository.orderitem.OrderItemRepository;
import org.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.example.bookstore.repository.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemsMapper orderItemsMapper;
    @Override
    @Transactional
    public OrderResponseDto createNewOrder(OrderRequestDto requestDto,
                                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id
                        + "does not exist"));
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(user.getId()).orElseThrow(()
                        -> new EntityNotFoundException("Shopping cart does not exist"));
        Set<CartItem> cartItems = shoppingCart.getCartItems();

        if (cartItems == null || cartItems.isEmpty()) {
            throw new EntityNotFoundException("No items in the shopping cart");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        order.setShippingAddress(requestDto.getShippingAddress());

        Set<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);

            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice().multiply(
                    BigDecimal.valueOf(cartItem.getQuantity())));

            return orderItem;
        }).collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        order.setTotal(orderItems.stream()
                .map(orderItem -> orderItem.getPrice().multiply(
                        BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        return orderMapper.toDto(savedOrder);
    }
}
