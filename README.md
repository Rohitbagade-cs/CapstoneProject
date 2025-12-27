
# 🏦Investment Banking Deal Pipeline Management Portal  

A **full-stack enterprise web application** designed to manage investment banking deals with **secure authentication**, **role-based authorization**, and **containerized deployment** using Docker.

---

## 📌 Project Overview

The **Deal Pipeline Management System** helps investment banks manage deals efficiently by providing:

* Secure user authentication using JWT
* Role-based access control (Admin & Analyst)
* Deal lifecycle management
* User management by Admin
* Full Dockerized deployment (Backend + Frontend + MySQL)

This project follows **industry-standard layered architecture** and **best security practices**.

---

## 🛠️ Tech Stack

### 🔹 Backend

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA (Hibernate)
* MySQL
* Maven

### 🔹 Frontend

* Angular
* TypeScript
* HTML / CSS
* JWT decoding
* HTTP Interceptors

### 🔹 DevOps / Deployment

* Docker
* Docker Compose
* Nginx (for Angular production build)

---

## 🧱 System Architecture

```
Frontend (Angular + Nginx)
        |
        |  HTTP + JWT
        |
Backend (Spring Boot APIs)
        |
        |  JPA / Hibernate
        |
Database (MySQL)
```

---

## 👥 User Roles & Permissions

### 🔐 ADMIN

* Login
* Create, view, edit, delete deals
* View & modify sensitive deal data (deal amount)
* Manage users (enable / disable users)
* Full system access

### 🔐 ANALYST

* Login
* Create deals
* View all deals
* Edit basic deal details
* Update deal status
* ❌ Cannot view deal amount
* ❌ Cannot delete deals
* ❌ Cannot manage users

---

## 🔑 Authentication & Security

* JWT-based authentication
* Stateless session management
* Passwords encrypted using BCrypt
* JWT stored securely in browser localStorage
* Role validation at:

  * Backend (Spring Security)
  * Frontend (Angular UI)

---

## 🚀 Application Flow

1. Application starts from `@SpringBootApplication`
2. Spring loads:

   * Controllers
   * Services
   * Repositories
   * Security filters
3. User logs in via `/api/auth/login`
4. JWT token generated and returned
5. Angular stores JWT and role
6. Every API request passes through JWT filter
7. Role is validated before accessing APIs
8. Business logic executed in Service layer
9. Data persisted via JPA repositories

---

## 📂 Project Structure

```
CAPSTONE-PROJECT
│
├── Backend
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   ├── security
│   └── config
│
├── Frontend
│   ├── components
│   ├── services
│   ├── interceptors
│   └── guards
│
├── docker-compose.yml
├── .env
└── README.md
```

---

## 📡 Backend APIs

### 🔐 Authentication

```
POST /api/auth/login
```

### 📄 Deals

```
GET    /api/deals        → ADMIN, ANALYST
POST   /api/deals        → ADMIN, ANALYST
PUT    /api/deals/{id}   → ADMIN, ANALYST
DELETE /api/deals/{id}   → ADMIN only
```

### 👤 User Management (ADMIN)

```
GET    /api/admin/users
PUT    /api/admin/users/{id}/enable
PUT    /api/admin/users/{id}/disable
```

---

## 🧪 Testing with Postman

1. Login using `/api/auth/login`
2. Copy JWT token
3. Add header:

```
Authorization: Bearer <JWT_TOKEN>
```

4. Test role-based APIs

---

## 🐳 Dockerized Deployment

### Services

* MySQL (with named volume)
* Spring Boot backend
* Angular frontend (served via Nginx)

### Run Command

```bash
docker compose up --build
```

### Access URLs

* Frontend: [http://localhost:4200](http://localhost:4200)
* Backend: [http://localhost:8080](http://localhost:8080)
* MySQL: localhost:3306

---

## ⚙️ Environment Variables (`.env`)

```
DB_NAME=deal_pipeline
DB_USERNAME=root
DB_PASSWORD=yourpassword
DB_URL=jdbc:mysql://mysql:3306/deal_pipeline

JWT_SECRET=dealpipelinejwtsecretkeydealpipelinejwt
```

---

## 🧠 Key Highlights

* Clean layered architecture
* Strong security implementation
* Role-based UI + API protection
* Production-ready Docker setup
* Easy scalability and maintainability

---
