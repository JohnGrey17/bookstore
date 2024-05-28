package org.example.bookstore.model.status;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    PENDING,
    COMPLETE,
    DELIVERED;

    @JsonCreator
    public static Status fromString(String str) {
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(str)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + str);
    }
}
