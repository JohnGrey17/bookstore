package org.example.bookstore.dto.orderdto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class OrderRequestDto {
    @Length(min = 7,max = 255)
    private String shippingAddress;
}
