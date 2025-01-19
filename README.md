# TroodProjectManagement

## Project Description
This is a backend for a project and vacancy management system. It uses **Kotlin**, **Spring Boot**, and **Firebase Firestore** for data storage.

## Technologies
- **Kotlin** – Primary programming language.
- **Spring Boot** – For building the REST API.
- **Firebase Firestore** – NoSQL database for storing projects and vacancies.
- **GitHub** – Version control and collaboration.

## How to Run the Project
1. **Clone the repository**
   ```sh
   git clone https://github.com/SuperShur1k/TroodProjectManagement.git
   cd TroodProjectManagement
## Run locally
  ./mvnw spring-boot:run
## API Endpoints
  #Project Management
    GET /projects – Get all projects.
    POST /projects – Create a new project.
    PUT /projects/{id} – Update a project.
    DELETE /projects/{id} – Delete a project.
  #Vacancy Management
    GET /projects/{id}/vacancies – Get all vacancies for a specific project.
    POST /projects/{id}/vacancies – Add a vacancy to a project.
    PUT /vacancies/{id} – Update a vacancy.
    DELETE /vacancies/{id} – Delete a vacancy.
