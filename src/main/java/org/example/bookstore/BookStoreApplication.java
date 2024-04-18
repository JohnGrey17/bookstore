package org.example.bookstore;

import java.math.BigDecimal;
import org.example.bookstore.model.Book;
import org.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);

    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setAuthor("Tolkien");
                book.setTitle("Lord_Of_The_ring");
                book.setPrice(BigDecimal.valueOf(100));
                book.setIsbn("45654g84");
                bookService.save(book);
                System.out.println(bookService.findAll());
            }
        };
    }
}

