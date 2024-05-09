package org.example.bookstore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Class<?> clazz = value.getClass();
        try {
            Field firstFieldValue = clazz.getDeclaredField(firstFieldName);
            firstFieldValue.setAccessible(true);
            Object firstObj = firstFieldValue.get(value);

            Field secondFieldValue = clazz.getDeclaredField(secondFieldName);
            secondFieldValue.setAccessible(true);
            Object secondObj = secondFieldValue.get(value);

            return Objects.equals(firstObj, secondObj);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Illegal access to field: " + e.getMessage());
        }
    }
}
