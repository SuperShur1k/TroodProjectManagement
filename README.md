# TroodProjectManagement

## 📌 Overview
TroodProjectManagement is a RESTful API built with Kotlin and Spring Boot for managing projects and vacancies. It integrates with Firebase as a database and provides endpoints for CRUD operations on projects and vacancies.

## 🚀 Features
- CRUD operations for **projects** and **vacancies**
- Firebase Firestore integration
- Global exception handling
- Input validation
- Unit and integration tests
- API documentation

---

## 🛠️ Tech Stack
- **Kotlin** 1.9.25
- **Spring Boot** 3.4.1
- **Firebase Firestore** (Database)
- **JUnit 5 & Mockito** (Testing)
- **Maven** (Build tool)
- **Docker** (Deployment)

---

## 📥 Installation & Setup
### 1️⃣ Prerequisites
- Install [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Install [Maven](https://maven.apache.org/install.html)
- Install [Docker](https://www.docker.com/)

### 2️⃣ Clone the Repository
```sh
git clone https://github.com/SuperShur1k/TroodProjectManagement.git
cd TroodProjectManagement
```

### 3️⃣ Set up Firebase Credentials
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/).
2. Go to **Project Settings → Service accounts**.
3. Click **Generate new private key** to download the `firebase-service-account.json` file.
4. Move this file to `src/main/resources/firebase/` in your project.
5. Ensure `application.properties` contains:
```properties
spring.application.name=TroodProjectManagement
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
spring.profiles.active=test

firebase.credentials=firebase/firebase-service-account.json
firebase.database-url=https://your-project-id.firebaseio.com
```

### 4️⃣ Build & Run the Application using Docker
1. Build the Docker image:
   ```sh
   docker build -t trood-project-management .
   ```
2. Run the container:
   ```sh
   docker run -d --name trood-management -p 8080:8080 trood-project-management
   ```
The server will start at `http://localhost:8080/`.

---

## 📖 API Endpoints & Usage
### **Projects**
| Method | Endpoint | Description | Example |
|--------|---------|-------------|---------|
| **GET** | `/projects` | Get all projects | `GET http://localhost:8080/projects` |
| **GET** | `/projects/{id}` | Get a project by ID | `GET http://localhost:8080/projects/1` |
| **POST** | `/projects` | Create a new project | See request example below |
| **PUT** | `/projects/{id}` | Update a project | See request example below |
| **DELETE** | `/projects/{id}` | Delete a project | `DELETE http://localhost:8080/projects/1` |

### **Vacancies**
| Method | Endpoint | Description | Example |
|--------|---------|-------------|---------|
| **GET** | `/projects/{projectId}/vacancies` | Get vacancies for a project | `GET http://localhost:8080/projects/1/vacancies` |
| **POST** | `/projects/{projectId}/vacancies` | Add a vacancy to a project | See request example below |
| **PUT** | `/vacancies/{vacancyId}` | Update a vacancy | See request example below |
| **DELETE** | `/vacancies/{vacancyId}` | Delete a vacancy | `DELETE http://localhost:8080/vacancies/1` |

### **Example API Requests**
#### **Create a Project**
```http
POST http://localhost:8080/projects
Content-Type: application/json

{
    "name": "New Project",
    "field": "IT",
    "experience": "2 years"
}
```

#### **Update a Project**
```http
PUT http://localhost:8080/projects/1
Content-Type: application/json

{
    "name": "Updated Project",
    "field": "Finance",
    "experience": "5 years"
}
```

#### **Create a Vacancy**
```http
POST http://localhost:8080/projects/1/vacancies
Content-Type: application/json

{
    "name": "Software Engineer",
    "field": "Development",
    "experience": "3 years",
    "country": "USA",
    "description": "Looking for a skilled developer."
}
```

#### **Update a Vacancy**
```http
PUT http://localhost:8080/vacancies/1
Content-Type: application/json

{
    "name": "Senior Software Engineer",
    "field": "Development",
    "experience": "5 years",
    "country": "USA",
    "description": "Looking for an experienced senior developer."
}
```

---

## 🧪 Running Tests
```sh
mvn test
```

---

## 📞 Contact
For any issues or feature requests, please open an issue on GitHub.

