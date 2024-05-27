package org.example.bookstore.dto.orderdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusUpdateRequestDto {
    @NotBlank
    private String status;
}
