# 💸 Expense Tracker – Personal Expense Management System
Expense Tracker is a modular Spring Boot–based application designed to help users track, manage, and analyze their personal and business expenses. The system offers secure 2FA authentication, real-time spending reports, and expense categorization.

This project demonstrates best practices in modular design, security (via JWT and OTP), and production-ready architecture choices.

# 🏦 Overview

The Expense Tracker project has been designed with best practices in mind, following a modular architecture and clean code principles. The focus has been on scalability, maintainability, and ensuring that the system is easy to extend in the future.

The application is designed to support multiple languages, ensuring that it can be easily adapted for international use, Messages (e.g., error messages) are externalized into resource files, allowing the system to support multiple languages.

The system allows users to:
- **Track and categorize expenses** for better financial organization.
- **Set monthly spending limits** and get alerts when limits are exceeded.
- **Generate monthly reports** for financial insights.
- **Secure authentication** with **JWT** and **OTP** login.
- **Manage user transactions and expenses efficiently**.


## 🔒 **Security**

**JWT-based Authentication** ensures secure, stateless sessions between users and the backend. **Two-Factor Authentication (2FA)** via **OTP** provides an additional layer of security.

Key security features:
- **JWT** tokens provide stateless and scalable authentication.
- **OTP** verification ensures only authorized users can access their accounts.
- **Redis** is used for OTP generation and rate-limiting, preventing abuse by limiting OTP requests.
- **Secure Password Storage**: User passwords are hashed using strong algorithms (e.g., BCrypt).

## ⚙️ **Tech Stack**

| **Component**          | **Technology**             |
|------------------------|----------------------------|
| **Language**           | Java 17                    |
| **Framework**          | Spring Boot                |
| **Database**           | PostgreSQL                 |
| **Authentication**     | JWT + OTP (via Redis)      |
| **Cache**              | Redis                      |
| **Containerization**   | Docker Compose             |
| **API Docs**           | Swagger / OpenAPI          |

## 🐳 **Docker Setup**

To run **Expense Tracker** locally using **Docker Compose**, follow these steps:

1. Clone the repository:
    ```bash
    git clone https://github.com/nedaasadi93/expense-tracker-api.git
    ```

2. Build and start the services:
    ```bash
    docker-compose up --build
    ```

## 📚 **API Documentation**

Once setting up the project and running the application, you can access the **API documentation** via Swagger UI at:

```bash
[Swagger UI](http://localhost:7000/api/v1/swagger-ui/index.html)
```





