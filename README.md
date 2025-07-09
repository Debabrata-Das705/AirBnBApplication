# 🏨 AirBnB Hotel Booking & Management Backend

A hotel booking and management backend system inspired by AirBnB, built using **Spring Boot** and **PostgreSQL**. This project provides secure APIs for hotel search, booking, payment processing, cancellation, and hotel management.

---

## 🚀 Features

- 🔐 **JWT Authentication & Role-Based Authorization**  
  Secure login system with roles for Admin, Hotel Manager, and Customer.

- 📦 **Hotel Search & Booking APIs**  
  Fully RESTful endpoints for searching hotels, booking rooms, and managing reservations.

- 💳 **Stripe Payment Gateway Integration**  
  Handles secure payments and supports refunds on booking cancellations.

- 📈 **Dynamic Pricing & Scheduling**  
  Applies surge pricing and availability logic using the **Decorator Design Pattern**.

- 📊 **Hotel Manager Dashboard**  
  Real-time overview of bookings, pricing, and room availability.

- 🧩 **Clean Architecture & Exception Handling**  
  Well-structured, modular codebase with consistent error handling.

---

## 🛠 Tech Stack

- **Backend:** Spring Boot
- **Database:** PostgreSQL
- **Security:** JWT, Spring Security
- **Payments:** Stripe API
- **API Style:** REST

---

## 📁 Project Structure
src/
├── config # Security & JWT configurations
├── controller # REST API controllers
├── dto # Data Transfer Objects
├── entity # JPA entities
├── exception # Custom exception handlers
├── repository # Spring Data JPA repositories
├── service # Business logic layer
├── util # Utility classes
└── ...

📬 API Documentation
Swagger UI is available at:
http://localhost:8080/api/v1/swagger-ui/index.html
