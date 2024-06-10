package org.example.bookstore.model.status;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    PENDING,
    COMPLETE,
    DELIVERED;

    @JsonCreator
    public static OrderStatus fromString(String str) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name().equalsIgnoreCase(str)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + str);
    }
}
