package org.example.bookstore.dto.userdto;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String shippingAddress;
}
