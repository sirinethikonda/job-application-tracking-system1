# Job Application Tracking System (ATS) – Backend

## 📌 Project Overview
The **Job Application Tracking System (ATS)** is a backend application designed to manage the complete hiring workflow for companies.  
It goes beyond basic CRUD operations by implementing **workflow state management, role-based access control (RBAC), asynchronous notifications, and audit logging**.

This project simulates **real-world enterprise backend architecture** using Java and Spring Boot.

---

## 🎯 Objectives
- Manage job postings and candidate applications
- Enforce a strict application workflow (state machine)
- Implement secure role-based access control
- Process notifications asynchronously
- Maintain a complete audit trail of application changes

---

## 👥 User Roles & Permissions

| Role | Capabilities |
|-----|--------------|
| **Candidate** | Apply for jobs, view own applications |
| **Recruiter** | Create jobs, manage applications |
| **Hiring Manager** | View applications at company level |

All endpoints are protected based on user roles.

---

## 🔁 Application Workflow (State Machine)

Applications follow a predefined workflow:
APPLIED → SCREENING → INTERVIEW → OFFER → HIRED
↘
REJECTED (from any stage)


- ❌ Invalid transitions are blocked
- ✅ Valid transitions only are allowed
- 🔒 Logic enforced in service layer

---

## 🧱 System Architecture

The project follows a **layered architecture**:

Controller → Service → Repository → Database
↓
State Machine
↓
RabbitMQ (Async Events)


This ensures clean separation of concerns and scalability.

---

## 🛠 Technology Stack

| Category | Technology |
|-------|-----------|
| Language | Java 17 / 21 |
| Framework | Spring Boot 3 |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA (Hibernate) |
| Database | MySQL |
| Messaging | RabbitMQ |
| Build Tool | Maven |
| API Testing | Postman |
| Containers | Docker, Docker Compose |
| IDE | STS4 / Eclipse |

---

## 🗄 Database Design

Main tables:
- `companies`
- `app_users`
- `jobs`
- `applications`
- `application_history`

Foreign keys ensure relational integrity.  
Each application stage change is recorded in `application_history`.

---

## 🔐 Authentication & Authorization

- JWT-based authentication
- Stateless session management
- Role-based access enforced using Spring Security
- Unauthorized access returns `403 Forbidden`

---

## 📬 Asynchronous Processing

- RabbitMQ is used for background processing
- Events are published when:
  - A candidate applies for a job
  - An application stage changes
- Consumers process notifications without blocking API responses

---

## 🚀 How to Run the Project

### 1️⃣ Start Infrastructure (MySQL + RabbitMQ)
🔌 API Endpoints Overview
Authentication

POST /auth/register

POST /auth/login

Jobs

GET /jobs

POST /jobs (Recruiter only)

DELETE /jobs/{id}

Applications

POST /jobs/{jobId}/apply

PATCH /applications/{id}/stage

GET /me/applications

GET /jobs/{jobId}/applications

🧪 Testing

APIs tested using Postman

Role-based access verified

Workflow transitions validated

Invalid transitions correctly blocked

Database state verified after each operation

✅ Key Highlights

✔ Workflow state machine
✔ Role-based access control
✔ Asynchronous notifications
✔ Audit logging
✔ Clean layered architecture
✔ Evaluation-ready backend system

📦 Future Enhancements

Email service integration (SendGrid / SES)

Pagination and filtering

Admin dashboard

Search functionality

Monitoring and logging

👤 Author

Ganisetti Sirinethi konda
Backend Developer – Java & Spring Boot

