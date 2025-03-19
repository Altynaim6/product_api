# Product API

## Overview

The **Product API** is a Spring Boot-based backend service that allows users to manage products, categories, and user accounts. The API enables functionalities like adding, updating, deleting products, retrieving products by seller, category, and date range, and managing user accounts. It utilizes Spring Data JPA for database interactions and custom exception handling to ensure smooth API behavior.

## Features

- **Product Management**: Allows CRUD operations for products.
- **Category Management**: Products can be categorized for better organization.
- **User Management**: Admin can manage user accounts and their roles.
- **Sorting**: Products can be retrieved by seller, category, or date range.
- **Custom Exception Handling**: All errors are handled through custom exceptions to provide meaningful error messages.

## Technologies Used

- **Spring Boot**: For building the backend application.
- **Spring Data JPA**: For database interaction and repositories.
- **H2 Database**: In-memory database for development.
- **JUnit 5**: For unit testing and integration testing.
- **Maven**: For dependency management and building the project.
- **ModelMapper**: For object mapping between entities and DTOs.
- **Postman**: For API testing.

## Endpoints

### Product Endpoints

#### 1. `GET /products`
- **Description**: Retrieves all products with pagination.
- **Parameters**:
    - `page` (int): Page number.
    - `size` (int): Page size.

#### 2. `GET /products/seller/{sellerId}`
- **Description**: Retrieves all products by a specific seller.
- **Parameters**:
    - `sellerId` (long): The ID of the seller.
    - `page` (int): Page number.
    - `size` (int): Page size.

#### 3. `GET /products/category/{categoryName}`
- **Description**: Retrieves all products by category.
- **Parameters**:
    - `categoryName` (String): The name of the category.
    - `page` (int): Page number.
    - `size` (int): Page size.

#### 4. `GET /products/date-range`
- **Description**: Retrieves products within a specified date range.
- **Parameters**:
    - `startDate` (LocalDate): Start of the date range.
    - `endDate` (LocalDate): End of the date range.
    - `page` (int): Page number.
    - `size` (int): Page size.

#### 5. `POST /products`
- **Description**: Adds a new product.
- **Parameters**:
    - `sellerId` (long): The seller's ID.
    - `categoryId` (long): The product's category ID.
    - `ProductRequest` (DTO): Product details (name, description, price, quantity).

#### 6. `PUT /products/{id}`
- **Description**: Updates an existing product.
- **Parameters**:
    - `id` (long): The product ID.
    - `categoryId` (long): The category ID (optional).
    - `ProductRequest` (DTO): Updated product details.

#### 7. `DELETE /products/{id}`
- **Description**: Deletes a product by ID.
- **Parameters**:
    - `id` (long): The product ID.

### User Endpoints

#### 1. `POST /users`
- **Description**: Registers a new user.
- **Parameters**:
    - `UserRequest` (DTO): User details (name, email, password, role).

#### 2. `GET /users/{id}`
- **Description**: Retrieves a user by ID.
- **Parameters**:
    - `id` (long): The user ID.

#### 3. `PUT /users/{id}`
- **Description**: Updates an existing user.
- **Parameters**:
    - `id` (long): The user ID.
    - `UserRequest` (DTO): Updated user details.

#### 4. `DELETE /users/{id}`
- **Description**: Deletes a user by ID.
- **Parameters**:
    - `id` (long): The user ID.

## Database Schema

### User Table (`users_tb`)

| Column Name | Type       | Description                       |
|-------------|------------|-----------------------------------|
| `id`        | `Long`     | Primary Key (auto-generated)      |
| `name`      | `String`   | User's name                       |
| `email`     | `String`   | User's email (unique, not null)   |
| `password`  | `String`   | User's password                   |
| `role`      | `String`   | User's role (Seller/Admin)        |

### Product Table (`product_tb`)

| Column Name | Type             | Description                             |
|-------------|------------------|-----------------------------------------|
| `id`        | `Long`           | Primary Key (auto-generated)            |
| `name`      | `String`         | Product's name                          |
| `description`| `String`        | Product's description                   |
| `price`     | `Double`         | Product's price                         |
| `quantity`  | `Double`         | Product's available quantity            |
| `createdAt` | `LocalDateTime`  | Date when the product was created       |
| `seller_id` | `Long`           | Foreign Key referencing `users_tb`      |
| `category_id`| `Long`          | Foreign Key referencing `category_tb`   |

## Run the Project

1. **Clone the Repository:**

```bash
git clone https://github.com/altynaim6/product-api.git
cd product-api
