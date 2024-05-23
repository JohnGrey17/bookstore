package org.example.bookstore.dto.orderDto;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.example.bookstore.dto.orderitemdto.OrderItemResponseDto;
import org.example.bookstore.model.OrderItem;
import org.example.bookstore.model.User;
import org.example.bookstore.model.status.Status;
import org.example.bookstore.model.status.StatusConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderResponseDto {

    private Long id;

    private User userId;

    private Set<OrderItemResponseDto> orderItems;

    private LocalDateTime orderDate;

    private BigDecimal total;

    private Status status;

}
