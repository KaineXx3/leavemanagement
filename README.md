🛑 Leave Management System
A Spring Boot backend application that manages employee leave requests and approvals.

🚀 Features
Employees can submit leave requests (sick leave, vacation, etc.)

Managers can view, approve, or reject leave requests

Employees can leave feedback for their leave requests

Leave balances are updated automatically upon approval

Cascade delete: leave requests and associated feedback are deleted when the employee is removed from the system

🛠️ Tech Stack
Java 17

Spring Boot

Spring Data JPA

Hibernate

PostgreSQL (or your database)

Redis (for caching leave balances)

Lombok


⚙️ How to Run
Option 1: Running with Maven
# 1. Clone the repo
git clone https://github.com/your-username/LeaveManagementSystem.git

# 2. Run the Spring Boot application
./mvnw spring-boot:run

Option 2: Running with Docker Compose
docker-compose up -d

