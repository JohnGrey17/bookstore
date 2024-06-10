<p align="center">
  <img src="Logo/bookStroreLogo" alt="Logo" width="400"/>
</p>

# JAVA-BOOKSTORE

**Introduction**

I am pleased to present to you my BookStore project. I approached the creation of this project with great responsibility and attention to detail. In it, I have implemented all the basic functions that an online store should have so that every book lover can easily find the books they are interested in and order them with just a few clicks.


## Content of presentation
- [Description of the project](#description-of-the-project)
- [How to Run the Application](#how-to-run-the-application)
- [Functionality](#functionality)
- [Challenges Faced During Development](#challenges-faced-during-development)
- [Technologies](#technologies)

## Description of the project

The main idea was to create a functionality that would allow performing the functions of an online bookstore.

* A user with the status (Administrator) can manage most processes in the bookstore (Add new books, add new book categories, delete books, etc.)
* Every potential user can enter the store and view all the books in the store without registration.
* After registration, the user receives the status (User), and the bookstore automatically creates a shopping cart for the user.
* Users can add books to the shopping cart, where they will wait for order creation.
* When a User decides to create an order, they only need to insert the shipping address, and the books from the shopping cart will move to the order.
* After the User presses "create a new order," all books from the shopping cart are marked as deleted and removed from the shopping cart, making it empty again.
* The new order gets the status (Pending), and after the user receives the order, the administrator can change the status to (Completed).
  In the future, users can check information about previous orders.

## How to Run the Application

**Prerequisites**
Ensure you have the following installed on your machine:

* **Java** **17**+
* **Maven** **4**+
* **MySQL** **8**+
* **Docker**

**To run the application locally, follow these steps:**

1. Clone the Repository

    [GitHub](https://github.com/JohnGrey17/bookStore.git)
2. Set up MySQL:
    Create a new MySQL database and note the database URL, username, and password.
3. Configure environment variables: Create a .env file in the project root directory and use that variables:        
```
MYSQL_DATABASE=your_db_name
MYSQL_USER_NAME=your_db_user_name  
MYSQL_ROOT_PASSWORD=your_db_password

SQL_HOST_NAME=localhost

SPRING_LOCAL_PORT=8088
SPRING_DOCKER_PORT=8080

SQL_LOCAL_PORT=3306
SQL_DOCKER_PORT=3308

DEBUG_PORT=5005

SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/books_store?serverTimezone=UTC

JWT_SECRET=bigmateacedemysecretfeb24blablablablablaoiuluzichervonakaluna
JWT_EXPIRATION=3000000000000000000

```
4. nstall dependencies and build the project:

```
mvn clean install
```
5. Run the application:
```
mvn spring-boot:run
```

**Using Docker**
1. Build the Docker image:
```
docker build -t bookStore .
```
2. Build and Run the Docker Containers:
```
docker-compose up
```

**Additional Information**

*Swagger Documentation:*

You can access the API documentation provided by Swagger at:

```
http://localhost:8080/swagger-ui/
```
## Functionality

_The API provides the following functionalities:_

- **Maneging Registration**
  * User registration
  * User authentication
- **Managing books**
  * Create new books _(For Administrators)_
  * Update book by book id _(For Administrators)_
  * Delete Book by book id _(For Administrators)_
  * Viewing the list of books
  * Searching for books by author and title
  * Find book by book id
- **Managing categories**
  * Create new category for book (_For administrators)_
  * Update category _(For administrators)_
  * Delete category _(For administrators)_
  * See all categories
  * Find category by id
  * Find book that related to category
- **Managing Shopping cart**
  * Adding books to the shopping cart
  * Find User shopping cart by id
  * Update existing shopping cart
  * Delete cart item in shopping cart by id
- **Managing orders**
  * Create new Order
  * Viewing order history
  * Get all order item by order id
  * Get information about order item
  *
- **Managing statuses of order**
  * Change status of order _(For administrators)_

---

### User

- **POST /api/auth/registration**: Register new user

Body

```
{
"email": "example@example.com",
  "password": "12345",
  "repeatPassword": "12345",
  "firstName": "Name",
  "lastName": "Last Name",
  "shippingAddress": "123 Main St, City, Country"
}
```

- **POST /api/auth/login** : Login user

Body

```
{
"email": "example@example.com",
  "password": "12345"
}
```

### Books

- **POST /api/books**: Create a new book (admin only)
  Body
  ```
  {
  "title": "Example Book",
  "author": "Example",
  "isbn": "978-3-16-148410-0",
  "price": 100,
  "description": "example of book description",
  "coverImage": "https://example.com/cover.jpg",
  "categoryIds": [1,3]
  }
  ```
- **PUT /api/books/{id}**: Update book by ID (admin only)
  Body

```
{
    "title":"New example",
    "author":"New example",
    "isbn":"978-3-16-148410-0",
    "price": 200,
    "description":"New Example ",
    "coverImage":"https://example.com/cover.jpg",
    "categoryIds":[2,3]
}
```

- **GET /api/books**: Get all books
- **GET /api/books/{id}**: Get book by ID
- **DELETE /api/books/{id}**: Delete book by ID (admin only)
- **GET /api/books/search**: Search books by author and title

### Categories

- **POST /api/categories**: Create a new category (admin only)
  Body

```
{
    "name": "Example",
    "description":"Example"
}
```

- **GET /api/categories**: Get all categories
- **GET /api/categories/{id}**: Get category by ID
- **PUT /api/categories/{id}**: Update category by ID (admin only)
  Body

```
{
    "name":"New Example",
    "description":"New example"
}
```

- **DELETE /api/categories/{id}**: Delete category by ID (admin only)
- **GET /api/categories/{id}/books**: Get books by category

### Shopping Cart

- **POST /api/cart**: Add a book to the shopping cart
  Body

```
{
  "bookId": 1,
  "quantity": 1
}
```

- **GET /api/cart/1** Get user's shopping cart by User ID (Admin only)
- **GET /api/cart:** Get current authorized user shopping cart
- **PUT /api/cart/{itemId}**: Update  quantity of cart Item of current authorized user
  Body

```
{
"quantity": 3
}
```

- **DELETE /api/cart/{itemId}**: Delete a cart item cart item ID

### Orders

- **POST /api/orders**: Create a new order
  Body

```
  {
  "shippingAddress": "Kyiv, Shevchenko ave, 1"
  }
```

- **GET /api/orders**: Get user's order history
- **GET /api/orders/{orderId}/items**: Get all order items by order ID
- **GET /api/orders/{orderId}/items/{itemId}**: Get detailed information about a specific order item
- **PATCH /api/orders/{id}** : Change the status of an order** (admin only)
  Body

```
{
    "status": "DELIVERED"
}
```

---

You can find collections of API request here

[POSTMAN COLLECTION](https://www.postman.com/rent-masters/workspace/online-book-store/collection/34439794-e43a7180-a360-47f8-86fa-fe7a81f5742e?action=share&source=copy-link&creator=34439794)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

---

## Challenges Faced During Development

Developing the BookStore project was an enriching experience, but it also came with its own set of challenges. Here are some of the key challenges I faced and the strategies I used to overcome them:

**1. Managing User Authentication and Authorization**

**Challenge:** Implementing secure user authentication and ensuring proper authorization for different user roles (Admin and User) was crucial.

**Solution:**

* I used JSON Web Tokens (JWT) for authentication. This involved creating a custom filter to intercept HTTP requests and validate JWT tokens.
* To manage user roles and permissions, I integrated Spring Security, which allowed me to easily define role-based access control for different endpoints.

**2. Handling Data Consistency and Transactions**

**Challenge**: Ensuring data consistency during operations like creating orders, updating shopping carts, and managing book inventory was complex, especially in a multi-user environment
**Solution:**

* I leveraged Spring’s @Transactional annotation to manage transactions effectively. This ensured that all database operations within a transaction were completed successfully, or none were applied at all, maintaining data integrity.
* For complex operations, I implemented appropriate locking mechanisms to prevent race conditions and ensure data consistency.

**3. Designing a Flexible and Scalable Data Model**

**Challenge:** Designing a data model that could handle various entities like books, categories, users, orders, and shopping carts while remaining flexible and scalable.

**Solution:**

* I used a relational database and designed the schema with normalization principles to ensure data integrity and minimize redundancy.
* To handle schema changes efficiently, I used Liquibase for database migration management. This allowed me to track, version, and deploy database schema changes reliably.

**4. Providing Detailed and User-Friendly API Documentation**
**Challenge**: Creating comprehensive and user-friendly API documentation that could help other developers understand and use the API effectively.

**Solution:**

* I integrated Swagger into the project. Swagger automatically generated interactive API documentation from my Spring Boot application, making it easier for users to explore and test the API endpoints.
* I also provided detailed descriptions, request/response examples, and parameter explanations to enhance the clarity and usability of the documentation.

**5. Containerizing the Application for Consistent Environments**

**Challenge**: Ensuring that the application runs consistently across different environments (development, testing, production) was crucial for smooth deployment and operation.: Ensuring that the application runs consistently across different environments (development, testing, production) was crucial for smooth deployment and operation.

**Solution:**

* I used Docker to containerize the application. Docker allowed me to package the application and its dependencies into a single container, ensuring consistency across various environments.
* I created Dockerfiles and Docker Compose configurations to simplify the process of building and running the application in different environments.

**6. Handling Error Management and User Feedback**
**Challenge**: Implementing a robust error-handling mechanism to provide meaningful feedback to users and developers when something goes wrong.

**Solution:**

* I developed a CustomGlobalExceptionHandler that captured and handled different types of exceptions globally. This ensured that users received clear and consistent error messages, and developers could easily identify and debug issues.
* I also used proper HTTP status codes to indicate the outcome of API requests, providing a better user experience.

By addressing these challenges with appropriate solutions, I was able to create a robust and scalable BookStore application that provides a seamless experience for both administrators and users.

## Technologies

- Java
- Spring Boot
- JWT for authentication
- MapStruct for DTO mapping
- Swagger for API documentation
- Docker for containerization
- Liquibase for database schema management
- CustomGlobalExceptionHandler for error handling
