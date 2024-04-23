package org.example.bookstore.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message, Exception e) {
        super(message);
    }
}
