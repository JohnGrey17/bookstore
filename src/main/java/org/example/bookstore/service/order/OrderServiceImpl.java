package org.example.bookstore.service.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.orderdto.OrderRequestDto;
import org.example.bookstore.dto.orderdto.OrderResponseDto;
import org.example.bookstore.dto.orderdto.OrderStatusUpdateRequestDto;
import org.example.bookstore.dto.orderdto.OrderUpdatedDto;
import org.example.bookstore.dto.orderitemdto.OrderItemResponseDto;
import org.example.bookstore.exception.EmptyCartException;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.OrderItemsMapper;
import org.example.bookstore.mapper.OrderMapper;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.OrderItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.model.order.Order;
import org.example.bookstore.model.status.OrderStatus;
import org.example.bookstore.repository.cartitem.CartItemRepository;
import org.example.bookstore.repository.order.OrderRepository;
import org.example.bookstore.repository.orderitem.OrderItemRepository;
import org.example.bookstore.service.shoppingcart.ShoppingCartService;
import org.example.bookstore.service.user.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemsMapper orderItemsMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderResponseDto createNewOrder(OrderRequestDto requestDto, Long userId) {
        User user = userService.getUserById(userId);
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(user.getId());
        Set<CartItem> cartItems = shoppingCart.getCartItems();

        if (cartItems == null || cartItems.isEmpty()) {
            throw new EmptyCartException("Your shopping cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(requestDto.getShippingAddress());

        Set<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> mapToOrderItem(cartItem, order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        order.setTotal(calculateTotal(orderItems));

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteAllByShoppingCartId(shoppingCart.getId());
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllUserOrders(Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);
        List<Order> ordersByUserId = orderRepository.findAllByUserId(user.getId());
        return ordersByUserId.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponseDto> getAllItemsByOrderId(Long orderId) {
        Order order = getOrderById(orderId);
        Set<OrderItem> orderItems = order.getOrderItems();

        return orderItems.stream()
                .map(orderItemsMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDto getItemByOrderIdAndItemId(Long orderId, Long itemId) {
        Order order = getOrderById(orderId);
        Set<OrderItem> orderItems = order.getOrderItems();
        return orderItems.stream()
                .filter(e -> Objects.equals(e.getId(), itemId))
                .map(orderItemsMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can't find item by id: " + itemId));
    }

    @Override
    @Transactional
    public OrderUpdatedDto changeStatusOfOrderById(Long orderId,
                                                   OrderStatusUpdateRequestDto request) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.valueOf(request.getStatus()));
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toUpdateDto(savedOrder);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId));
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice().multiply(
                        BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderItem mapToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        return orderItem;
    }
}
