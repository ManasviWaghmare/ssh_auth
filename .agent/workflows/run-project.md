---
description: How to run the SSH Authentication Module project
---

# Running the SSH Authentication Module

Follow these steps to build and run the backend application and the optional frontend.

## Prerequisites
- Java 17
- Maven 3.6+

## Backend Setup

1. **Create the data directory** (if it doesn't exist):
   ```bash
   mkdir -p data
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

The backend API will be available at `http://localhost:8080`.
You can access the **Swagger UI** for API testing at `http://localhost:8080/swagger-ui.html`.

## Frontend Setup (Optional)

1. **Navigate to the frontend directory**:
   ```bash
   cd frontend
   ```

2. **Serve the static files**:
   ```bash
   python3 -m http.server 3000
   ```

Access the frontend dashboard at `http://localhost:3000`.
