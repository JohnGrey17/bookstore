package org.example.bookstore.dto.orderDto;


import lombok.Data;
import org.example.bookstore.dto.orderitemdto.OrderItemResponseDto;
import org.example.bookstore.model.User;
import org.example.bookstore.model.status.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderResponseDto {

    private Long id;

    private Long userId;

    private Set<OrderItemResponseDto> orderItems;

    private LocalDateTime orderDate;

    private BigDecimal total;

    private Status status;

    private String shippingAddress;

}
