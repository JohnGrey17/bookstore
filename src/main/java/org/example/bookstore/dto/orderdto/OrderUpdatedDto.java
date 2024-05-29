package org.example.bookstore.dto.orderdto;

import jakarta.validation.constraints.NotNull;
import org.example.bookstore.model.status.OrderStatus;

public class OrderUpdatedDto {
    @NotNull
    private OrderStatus status;
}
