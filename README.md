# project-backend

Backend API for the hospital management system.

## Stack
- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- MySQL
- JWT

## Setup

```powershell
cd project-backend
set DB_USERNAME=root
set DB_PASSWORD=root
set JWT_SECRET=HospitalManagementSystemSecretKey1234567890
mvn clean install
mvn spring-boot:run
```

## Database
Create the `hospital_db` database and run the schema script in the `database` folder before starting the app.

## Notes
- Store backend-only configuration here.
- Do not mix frontend UI code into this repository.
