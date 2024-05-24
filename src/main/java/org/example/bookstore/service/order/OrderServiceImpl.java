package org.example.bookstore.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.orderdto.OrderRequestDto;
import org.example.bookstore.dto.orderdto.OrderResponseDto;
import org.example.bookstore.dto.orderdto.OrderStatusUpdateRequest;
import org.example.bookstore.dto.orderdto.OrderUpdatedDto;
import org.example.bookstore.dto.orderitemdto.OrderItemResponseDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.OrderItemsMapper;
import org.example.bookstore.mapper.OrderMapper;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.OrderItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.model.order.Order;
import org.example.bookstore.model.status.Status;
import org.example.bookstore.repository.order.OrderRepository;
import org.example.bookstore.repository.orderitem.OrderItemRepository;
import org.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.example.bookstore.repository.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + user.getId()
                        + "does not exist"));
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(user.getId()).orElseThrow(()
                        -> new EntityNotFoundException("Shopping cart does not exist"));
        Set<CartItem> cartItems = shoppingCart.getCartItems();

        if (cartItems == null || cartItems.isEmpty()) {
            throw new EntityNotFoundException("Your shopping cart is empty ");
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
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getAllUserOrders(Pageable pageable) {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<OrderItemResponseDto> getAllItemsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("can`t find "
                        + "order by id: " + orderId));
        Set<OrderItem> orderItems = order.getOrderItems();

        return orderItems.stream()
                .map(orderItemsMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderItemResponseDto getItemByOrderIdAndItemId(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("can`t find "
                        + "order by id: " + orderId));
        Set<OrderItem> orderItems = order.getOrderItems();
        return orderItems.stream().filter(e -> Objects.equals(e.getId(), itemId))
                .map(orderItemsMapper::toDto).findFirst().get();
    }

    @Override
    @Transactional
    public OrderUpdatedDto changeStatusOfOrderById(Long orderId, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException("Order with id: " + orderId + " does not exist"));

        Status newStatus = Arrays.stream(Status.values())
                .filter(sts -> sts.name().equalsIgnoreCase(request.getStatus().trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: "
                        + request.getStatus()));

        order.setStatus(newStatus);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toUpdateDto(savedOrder);
    }
}
