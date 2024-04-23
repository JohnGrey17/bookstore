package org.example.bookstore.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import lombok.AllArgsConstructor;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.BookRepository;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Book save(Book book) {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new EntityNotFoundException("Can't save book " + book);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("FROM Book", Book.class).getResultList();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't find any book ");
        }
    }

    @Override
    public Book getBookById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(Book.class,id);
        } catch (Exception e) {
            throw new EntityNotFoundException("can`t find book with that id " + id);
        }
    }
}
