package org.example.bookstore.repository.category;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT c FROM Category c WHERE c.id IN :ids")
    List<Category> findCategoriesByIds(@Param("ids") Set<Long> ids);

    Optional<Category> findByName(String categoryName);
}
