package org.example.bookstore.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.bookstore.repository.cartitem.model.Category;
import org.example.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        "classpath:database/categories/add-categories-to-categories-table.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {
        "classpath:database/categories/remove-categories-from-books-table.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class CategoryRepositoryTest {
    private static final Set<Long> CORRECT_CATEGORIES_ID = Set.of(10L, 11L);
    private static final Set<Long> INCORRECT_CATEGORIES_ID = Set.of(8L, 9L);
    private static final Integer EXPECTED_CATEGORIES_SIZE = 2;
    private static final String CORRECT_CATEGORIES_NAME = "Drama";
    private static final String INCORRECT_CATEGORIES_NAME = "Rammstein";

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find categories by Id")
    void findCategoriesByIds_withExistingId_positiveResult() {
        List<Category> actualCategories = categoryRepository.findCategoriesByIds(CORRECT_CATEGORIES_ID);
        Assertions.assertNotNull(actualCategories, "Categories should be present");
        Assertions.assertEquals(EXPECTED_CATEGORIES_SIZE, actualCategories.size(),
                "Result size should be match with expected size");
    }

    @Test
    @DisplayName("Give negative result when the Categories id is wrong")
    void findCategoriesByIds_withNonExistingId_negativeResult() {
        List<Category> actualCategories = categoryRepository.findCategoriesByIds(INCORRECT_CATEGORIES_ID);
        Assertions.assertTrue(actualCategories.isEmpty(), "Categories should not be present");
    }

    @Test
    @DisplayName("Find categories by categories`s name")
    void findByName_withCorrectName_positiveResult() {
        Optional<Category> categoryByName = categoryRepository.findByName(CORRECT_CATEGORIES_NAME);

        Assertions.assertTrue(categoryByName.isPresent(), "Category should be present");

        Category actual = categoryByName.get();

        Assertions.assertEquals(CORRECT_CATEGORIES_NAME, actual.getName(),
                "Category name should be match");
    }

    @Test
    @DisplayName("Give negative result when the Categories name is wrong")
    void findByName_withIncorrectName_negativeResult() {
        Optional<Category> categoryByWrongName
                = categoryRepository.findByName(INCORRECT_CATEGORIES_NAME);

        Assertions.assertFalse(categoryByWrongName.isPresent(), "Category should not be present");

    }

    @Test
    @DisplayName("Find categories by Ids with existing Ids - positive case")
    void findCategoriesByIds_existingIds_shouldReturnCategories() {
        List<Category> actualCategories = categoryRepository.findCategoriesByIds(CORRECT_CATEGORIES_ID);

        Assertions.assertNotNull(actualCategories, "Categories should be present");
        Assertions.assertEquals(EXPECTED_CATEGORIES_SIZE, actualCategories.size(),
                "Result size should match the expected size");

        for (Category category : actualCategories) {
            Assertions.assertNotNull(category.getId(), "Category ID should not be null");
            Assertions.assertTrue(CORRECT_CATEGORIES_ID.contains(category.getId()),
                    "Category ID should be in the correct ID set");
        }
    }
}