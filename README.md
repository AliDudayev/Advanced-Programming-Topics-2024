![image](https://github.com/user-attachments/assets/ca1694ef-f1ac-4711-83b4-8fb530da660a)# Advanced-Programming-Topics-2024

This project allows users to track their fitness journey by logging personal progress and viewing detailed statistics over time. The system provides users with an easy way to monitor their health-related activities, track workouts, and visualize their personal records.

## Contributors
- **Magomed-Ali Dudayev**: [AliDudayev](https://github.com/AliDudayev)
- **Marnick Michielsen**: [MarnickM](https://github.com/MarnickM)

---

## Project Overview

The application consists of a microservices architecture built with Docker Compose. The primary goal is to ensure modularity, scalability, and maintainability by segregating core functionalities into different services. Users can interact with the system through a unified web interface that communicates with the services via an API Gateway.

### Key Features:
- **User Management**: Create, update, and manage users.
- **Health Tracking**: Log and view health records.
- **Workout Management**: Log workouts and retrieve workout statistics.
- **Record Keeping**: Maintain a history of personal records made during a workout of each user.

---

## Implemented items from assignement

### Basics:
- 4 microservices with 2x MongoDB and 2x MySQL
- API gateway with OAuth2 secured/unsecured endpoints
- Docker Compose deployment with Github Actions
- Unit tests for all service classes

### Extra:
- (2.1 => 15%) Frontend in seperate container that can use the microservices via the API gateway

---

## Technologies Used

### Backend
- **Spring Boot**: Framework for microservices implementation.
- **Spring Cloud Gateway**: Handles API Gateway routing and filters.
- **OAuth2**: Used for secure authentication and authorization.

### Frontend
- **Web Interface**: A Spring Web framework with Thymeleaf for user interaction.

### Databases
- **MySQL**: Used for the `health-service` and `workout-service`.
- **MongoDB**: Used for the `user-service` and `record-service`.

### Deployment
- **Docker Compose**: Orchestrates multi-container Docker applications for local deployment.

---

## Architecture Diagram

The system's deployment diagram illustrates the integration of various microservices, their databases, and how they are connected via the API Gateway. Below is a visual representation of the architecture:

![Deployment Diagram](Schema_deployment.drawio.png)

---

## Services and Endpoints

### **All Endpoints**

#### Health Service
- **GET** `/health/all` -> Get all health records
- **GET** `/health` -> Get a health record
- **POST** `/health` -> Save a health record
- **PUT** `/health` -> Update a health record

#### Record Service
- **GET** `/record/all` -> Get all records
- **GET** `/record` -> Get a record
- **POST** `/record` -> Save a record
- **PUT** `/record` -> Update a record
- **DELETE** `/record` -> Delete a record

#### User Service
- **GET** `/user/all` -> Get all users
- **GET** `/user` -> Get a user
- **POST** `/user` -> Save a user
- **PUT** `/user` -> Update a user
- **DELETE** `/user` -> Delete a user
- **GET** `/user/record` -> Get the record of a user (from record-service)
- **GET** `/user/record/all` -> Get all records (from record-service)
- **POST** `/user/record` -> Save a record of a user (in record-service)
- **PUT** `/user/record` -> Update a record of a user (in record-service)
- **DELETE** `/user/record` -> Delete a record of a user (in record-service)
- **GET** `/user/workout` -> Get all workouts from a user (from workout-service)

#### Workout Service
- **GET** `/workout/all` -> Get all workouts
- **GET** `/workout` -> Get a workout
- **POST** `/workout` -> Save a workout
- **GET** `/workout/user` -> Get workouts by user
- **GET** `/workout/health` -> Get health record of a workout (from health-service)

---
## Docker Compose structure

In the image below, you can see how our docker compose file is structured to build the entire setup.

![Docker Compose structure](Docker_Compose.drawio.png)

---

## How to Run Locally

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/AliDudayev/Advanced-Programming-Topics-2024.git
   cd Advanced-Programming-Topics-2024
   ```

2. **Set Up Environment Variables:**
   Create a `.env` file in the root directory with the following variables:
   ```env
   GOOGLE_CLIENT_ID=<your-google-client-id>
   GOOGLE_CLIENT_SECRET=<your-google-client-secret>
   HEALTH_SERVICE_BASEURL=localhost:8084
   RECORD_SERVICE_BASEURL=localhost:8082
   USER_SERVICE_BASEURL=localhost:8081
   WORKOUT_SERVICE_BASEURL=localhost:8083
   ```

3. **Start Services with Docker Compose:**
   ```bash
   docker-compose up -d
   ```

4. **Access the Web Interface:**
   Navigate to `http://localhost:8086` in your web browser.
   Note, not all endpoints are available in the webbrowser. Only the ones that support the goal of our webinterface are integrated, all others can be reached via Postman though.

5. **Getting a valid token for authentication:**
   Open Postman an make a new request, in the `authorization tab` select `OAuth 2.0`.
   
   ![afbeelding](https://github.com/user-attachments/assets/76de8dc8-cf6c-4b49-957c-6ceefc71590a)

   After this scroll down to `configure new token` and fill in the following URLs and credentials. Note, you will need to first setup OAuth2 in your Google project to get these URLs and credentials.
   
   ![afbeelding](https://github.com/user-attachments/assets/229f95ed-90d6-432d-a9d6-ca32b59273ab)

   Finally press on the `Get new access token` button at the bottom to get your token.
   
   ![afbeelding](https://github.com/user-attachments/assets/e7929e96-9757-47c8-ab39-609971ca8ab3)

---

## Postman screenshots

**User**

GET /user/all
![image](https://github.com/user-attachments/assets/75d8ab3d-f962-44ea-9bd0-34cf3e2a7365)

GET /user
![image](https://github.com/user-attachments/assets/9315d074-5f9d-4d3d-a72c-26e0d3931976)

POST /user
![image](https://github.com/user-attachments/assets/2af1383b-7bf1-4797-bef4-cc8dcfd9621f)

PUT /user
![image](https://github.com/user-attachments/assets/8df96396-b3cc-42eb-8d03-e9c60de08bd7)

DELETE /user
![image](https://github.com/user-attachments/assets/4ea1726e-37cf-4701-b72c-7a2d280ea35a)

GET /user/record/all
![image](https://github.com/user-attachments/assets/0b695e2c-3d80-4f4e-9977-d3f5730826a8)

GET /user/record
![image](https://github.com/user-attachments/assets/5e9dc356-6232-492f-ae2b-dfbc76365dc5)

POST /user/record
![image](https://github.com/user-attachments/assets/a510994d-4aad-48a1-9817-5f55e0545b2d)

PUT /user/record
![image](https://github.com/user-attachments/assets/faaf0215-dc6f-44cc-be99-c05c43bc37b8)

DELETE /user/record
![image](https://github.com/user-attachments/assets/fbd9a5fd-329e-4ea3-9f7a-f0f403718fda)

GET /user/workout
![image](https://github.com/user-attachments/assets/18e87697-488b-4278-b1c5-cb5d3f58b5af)

**Record**

GET /record/all
![image](https://github.com/user-attachments/assets/13eb747c-0984-4560-9a60-6d4d233e4c0c)

GET /record
![image](https://github.com/user-attachments/assets/5b9f118e-57c2-489e-a1b4-4ea6ef99c446)

POST /record
![image](https://github.com/user-attachments/assets/168c299b-82ef-4513-9086-6ad9be50dc3a)

PUT /record
![image](https://github.com/user-attachments/assets/e490fa90-d5d7-400e-a15e-f53f2d0edc9c)

DELETE /record
![image](https://github.com/user-attachments/assets/3e329e00-ed33-453d-9c1c-9f338d944693)

**Workout**

GET /workout/all
![image](https://github.com/user-attachments/assets/d113457d-6d7f-44ea-9358-af6744543045)

GET /workout
![image](https://github.com/user-attachments/assets/776adc09-43ae-4e02-b16a-821721874b28)

POST /workout
![image](https://github.com/user-attachments/assets/2443ada0-a4a9-4382-a8c7-75ea1eeb59c3)

GET /workout/user
![image](https://github.com/user-attachments/assets/aad0fdfa-bc02-4fe9-8b9b-166684575069)

GET /workout/health

**Health**

GET /health/all
![image](https://github.com/user-attachments/assets/4df91bcf-13b4-495e-9dea-752f6ddceef8)

GET /health
![image](https://github.com/user-attachments/assets/c0defa27-09b8-4e76-86c1-c41b82152838)

POST /health
![image](https://github.com/user-attachments/assets/1ceadc80-5098-4695-b256-0f34b683111c)

PUT /health
![image](https://github.com/user-attachments/assets/a2c7a04e-ce8c-40e4-a348-59b65315782a)

