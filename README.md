# Product API

## 📌 Overview

**Product API** is a Spring Boot RESTful application that supports user authentication features such as registration, login, refresh token handling, and email verification.

---

## 🚀 Features

- ✅ User registration with email verification
- 🔐 Login with JWT authentication (access + refresh tokens)
- 🔄 Secure token refresh mechanism
- 🧑‍🤝‍🧑 Role-based user management (e.g., `CUSTOMER`)
- 📧 Email sending for account verification

---

## 🛠️ Technologies

- Java 17+
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA (Hibernate)
- Lombok
- PostgreSQL / H2
- JUnit 5 & Mockito

---

## 🧰 Getting Started

### ✅ Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL or H2 database
- SMTP email account (e.g. Gmail)

### 📥 Installation

Configure the application.properties file:
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/productdb
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=youremail@gmail.com
spring.mail.password=yourpassword
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# JWT
jwt.secret=yourSecretKey
jwt.expiration.access=900000
jwt.expiration.refresh=86400000

Build and run the application:
mvn spring-boot:run


## 📡 API Endpoints

| Method | Endpoint        | Description                             |
|--------|------------------|-----------------------------------------|
| POST   | `/auth/register` | Register a new user                     |
| POST   | `/auth/login`    | Log in and receive JWT tokens           |
| POST   | `/auth/refresh`  | Refresh access token                    |
| GET    | `/auth/verify`   | Verify user email and activate account |

---

## 🧪 Running Tests

Run unit tests with:

```bash
mvn test

