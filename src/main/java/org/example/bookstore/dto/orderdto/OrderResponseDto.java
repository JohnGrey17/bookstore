package org.example.bookstore.dto.orderdto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import org.example.bookstore.dto.orderitemdto.OrderItemResponseDto;
import org.example.bookstore.model.status.Status;

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
