package org.example.bookstore.dto.categorydto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryRequestDto {
    @NotBlank
    private String name;
    @Length(min = 10,max = 255)
    private String description;
}
