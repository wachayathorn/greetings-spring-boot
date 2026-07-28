# greetings-spring-boot

MVP CRUD Greetings API — Java 21 + Spring Boot 3.5 + PostgreSQL

## Stack

- Spring Web + Validation
- Spring Data JPA + Flyway
- PostgreSQL (docker-compose)
- springdoc-openapi (Swagger UI)
- Tests: Service (Mockito), Controller (`@WebMvcTest`), Repository (`@DataJpaTest` + H2)

## Quick start

### 1) Start database

```bash
docker compose up -d
```

### 2) Run app

```bash
./run.sh
# หรือ
mvn spring-boot:run
```

### 3) Try API

```bash
# create
curl -s -X POST http://localhost:8080/api/v1/greetings \
  -H 'Content-Type: application/json' \
  -d '{"name":"Wachayathorn"}'

# list
curl -s http://localhost:8080/api/v1/greetings

# get by id
curl -s http://localhost:8080/api/v1/greetings/1

# update
curl -s -X PUT http://localhost:8080/api/v1/greetings/1 \
  -H 'Content-Type: application/json' \
  -d '{"name":"Updated"}'

# delete
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE http://localhost:8080/api/v1/greetings/1
```

Swagger UI: http://localhost:8080/swagger-ui.html

## Tests

```bash
mvn test
```

## Project layout

```
src/main/java/com/wachayathorn/greetings/
├── GreetingsApplication.java
├── common/
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
└── greeting/
    ├── GreetingController.java
    ├── GreetingService.java
    ├── GreetingRepository.java
    ├── GreetingEntity.java
    ├── GreetingNotFoundException.java
    └── dto/
        ├── CreateGreetingRequest.java
        ├── UpdateGreetingRequest.java
        └── GreetingResponse.java
```

Layering (MVP): **Controller → Service → Repository → PostgreSQL**
