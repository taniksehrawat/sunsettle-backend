☀️ SunSettle – Solar Energy Monitoring & Billing SaaS

SunSettle is a backend SaaS platform for solar energy monitoring and automated billing.
The system processes solar energy generation data and generates monthly bills with GST calculations.

This project demonstrates Java backend development using Spring Boot, REST APIs, and database integration.

🚀 Features

📊 Solar Energy Data Processing
Import solar generation data using CSV files.

🧾 Automated Billing System
Generates monthly bills based on energy production.

💰 GST Calculations
Automatically calculates GST in billing.

🔌 RESTful APIs
Backend APIs for managing solar data and billing.

☁️ Cloud Deployment
Application deployed on Render.

🛠 Tech Stack

Backend

Java

Spring Boot

Database

MySQL

Tools & Deployment

Maven

Git & GitHub

Render (Cloud Deployment)

🏗 Project Architecture

The project follows layered architecture.

Controller → Service → Repository → Database

Structure:

src/main/java/com/sunsettle

controller
 └── HelloController

service
 └── HelloService

model
 └── User

repository
 └── UserRepository

This structure keeps the code clean, modular, and scalable.

⚙️ Setup & Installation
1️⃣ Clone the Repository
git clone https://github.com/taniksehrawat/sunsettle-backend.git
cd sunsettle-backend
2️⃣ Configure Database

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/sunsettle
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
3️⃣ Run the Application
mvn spring-boot:run

Server will start on:

http://localhost:8080
🔗 API Example
Test API
GET /hello

Response

Hello from SunSettle Backend
📌 Future Improvements

User authentication with JWT

Solar plant and inverter management

Energy analytics dashboard APIs

Billing PDF generation

Notification system for billing

👨‍💻 Author

Tanik Sehrawat

GitHub
https://github.com/taniksehrawat

LinkedIn
https://www.linkedin.com/in/tanik-sehrawat-651134295

⭐ If you like this project, feel free to star the repository.
