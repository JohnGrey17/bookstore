package org.example.bookstore.service.order;

import java.math.BigDecimal;
import java.util.List;
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
import org.example.bookstore.exception.OrderException;
import org.example.bookstore.mapper.OrderItemsMapper;
import org.example.bookstore.mapper.OrderMapper;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.OrderItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.model.order.Order;
import org.example.bookstore.model.roles.RoleName;
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
    public OrderResponseDto createNewOrder(
            OrderRequestDto requestDto,
            Long userId) {
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
    public List<OrderResponseDto> getAllUserOrders(
            Long userId,
            Pageable pageable) {
        User user = userService.getUserById(userId);
        List<Order> ordersByUserId = orderRepository.findAllByUserId(user.getId());
        return ordersByUserId.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponseDto> getAllItemsByOrderId(
            Long orderId,
            Long userId) {
        User user = userService.getUserById(userId);
        boolean checkUserRole = checkUserRole(user);

        if (checkUserRole) {
            Order order = orderRepository.findById(orderId).orElseThrow(
                    () -> new OrderException("order with id: " + orderId + "does not exist"));
            Set<OrderItem> orderItems = order.getOrderItems();
            return orderItems.stream().map(orderItemsMapper::toDto).toList();
        }

        List<Order> allByUserId = orderRepository.findAllByUserId(user.getId());
        Order order = allByUserId.stream()
                .filter(o -> orderId.equals(o.getId()))
                .findFirst().orElseThrow(
                    () -> new OrderException("User dont have order with id: " + orderId));
        return order.getOrderItems()
                .stream()
                .map((orderItemsMapper::toDto))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDto getItemByOrderIdAndItemId(
            Long orderId,
            Long itemId,
            Long userId) {
        User user = userService.getUserById(userId);

        boolean isAdmin = checkUserRole(user);
        Order order;

        if (isAdmin) {
            order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException(
                    "Order with id: " + orderId + " does not exist"));
        } else {
            order = orderRepository.findByUserIdAndId(userId, orderId).orElseThrow(
                    () -> new OrderException(
                    "Order with id: " + orderId + " does not exist for the user"));
        }

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new OrderException(
                        "Item with id: " + itemId + " does not exist in the order"));

        return orderItemsMapper.toDto(orderItem);
    }

    @Override
    @Transactional
    public OrderUpdatedDto changeStatusOfOrderById(
            Long orderId,
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

    private OrderItem mapToOrderItem(
            CartItem cartItem,
            Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        return orderItem;
    }

    private boolean checkUserRole(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(RoleName.ADMIN));
    }
}
