package org.example.bookstore.repository.orderitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItem extends JpaRepository<org.example.bookstore.model.OrderItem,Long> {
}
