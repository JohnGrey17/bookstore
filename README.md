<p align="center">
  <img src="Logo/bookStroreLogo" alt="Logo" width="400"/>
</p>

# JAVA-BOOKSTORE

**Introduction**

I am pleased to present to you my BookStore project. I approached the creation of this project with great responsibility and attention to detail. In it, I have implemented all the basic functions that an online store should have so that every book lover can easily find the books they are interested in and order them with just a few clicks.

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
----

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
- **GET /api/cart**: Get user's shopping cart
- **PUT /api/cart/{itemId}**: Update a cart item by cart item ID
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

## Technologies

- Java
- Spring Boot
- JWT for authentication
- MapStruct for DTO mapping
- Swagger for API documentation
- Docker for containerization
- Liquibase for database schema management
- CustomGlobalExceptionHandler for error handling
