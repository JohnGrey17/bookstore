package org.example.bookstore.dto.orderdto;

import jakarta.validation.constraints.NotNull;
import org.example.bookstore.model.status.Status;

public class OrderUpdatedDto {
    @NotNull
    private Status status;
}
