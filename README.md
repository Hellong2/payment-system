# Project Overview

This Spring Boot application is designed to process payment transactions, integrating with SOAP and Kafka. It includes:

- **REST API**: Provides CRUD operations for payment transactions.
- **SOAP API**: Handles payment creation and correction requests.
- **Kafka Integration**: Publishes messages for completed and expired transactions.
- **Scheduled Task**: Monitors transaction statuses and triggers actions accordingly.

## Prerequisites

- **Java Development Kit (JDK)**: Ensure you have a compatible JDK installed.
- **Maven or Gradle**: For building and managing dependencies.
- **Docker**: For containerizing the application and Kafka.

## Getting Started

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   ```
2. **Build the Project**:
   ```bash
   mvn clean package
   ```
3. **Run the Application**:

   ```bash
   java -jar target/your-project-name.jar
   ```
## Docker Usage
1. **Build the Docker Image**:

   ```bash
   docker build -t your-image-name .
   ```
   
2. **Run the Docker Container**:
   ```bash
   docker run -p 8080:8080 your-image-name
   ```

## API Documentation
The API documentation is available at: http://localhost:8080/swagger-ui/

## Kafka Topics
completed_transactions: Publishes messages for completed transactions.

expired_transactions: Publishes messages for expired transactions.

## Configuration
The application configuration is located in the application.yml or application.properties file. You can customize properties like database connection details, Kafka broker addresses, and more.

## Testing
Unit and integration tests are included to ensure code quality and functionality. You can run the tests using your preferred testing framework (e.g., JUnit, TestNG).
