# Greetings Spring Boot

CRUD API สำหรับฝึก **Java 21 + Spring Boot 3.5**  
ชั้นงานแบบ MVP: `Controller → Service → Repository → PostgreSQL`

---

## Features

- RESTful CRUD (`/api/v1/greetings`)
- Bean Validation + global exception handler
- Spring Data JPA + Flyway migration
- PostgreSQL ผ่าน Docker Compose
- Swagger UI (springdoc-openapi)
- Unit tests ครบทุกชั้น (Service / Controller / Repository)

---

## Prerequisites

| Tool | หมายเหตุ |
|---|---|
| **JDK 21** | แนะนำ LTS (โปรเจกต์รองรับ `.jdks/` ในเครื่องด้วย) |
| **Docker** | สำหรับ PostgreSQL |
| **Maven 3.9+** | หรือใช้ `./run.sh` ที่ชี้ Maven ใน `.tools/` |

---

## Quick start

### 1. Start PostgreSQL

```bash
docker compose up -d
```

### 2. Run the app

```bash
./run.sh
```

`./run.sh` จะตั้ง `JAVA_HOME` + Maven ให้แล้วรัน `spring-boot:run`

คำสั่งอื่นผ่านสคริปต์เดียวกันได้ เช่น:

```bash
./run.sh test
./run.sh -v
```

### 3. Open Swagger

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## API

Base path: `/api/v1/greetings`

| Method | Path | Description | Status |
|---|---|---|---|
| `POST` | `/api/v1/greetings` | Create | `201` |
| `GET` | `/api/v1/greetings` | List all | `200` |
| `GET` | `/api/v1/greetings/{id}` | Get by id | `200` / `404` |
| `PUT` | `/api/v1/greetings/{id}` | Update name | `200` / `404` |
| `DELETE` | `/api/v1/greetings/{id}` | Delete | `204` / `404` |

### Examples

```bash
# Create
curl -s -X POST http://localhost:8080/api/v1/greetings \
  -H 'Content-Type: application/json' \
  -d '{"name":"Wachayathorn"}'

# List
curl -s http://localhost:8080/api/v1/greetings

# Get by id
curl -s http://localhost:8080/api/v1/greetings/1

# Update
curl -s -X PUT http://localhost:8080/api/v1/greetings/1 \
  -H 'Content-Type: application/json' \
  -d '{"name":"Updated"}'

# Delete
curl -s -o /dev/null -w "%{http_code}\n" \
  -X DELETE http://localhost:8080/api/v1/greetings/1
```

### Table: `greetings`

| Column | Type | Notes |
|---|---|---|
| `id` | `BIGSERIAL` | Primary key |
| `name` | `VARCHAR(100)` | Required |
| `created_at` | `TIMESTAMPTZ` | Set on create |

---

## Tests

```bash
./run.sh test
```

| Layer | Style |
|---|---|
| Service | JUnit 5 + Mockito |
| Controller | `@WebMvcTest` + MockMvc |
| Repository | `@DataJpaTest` + H2 |

---

## Project structure

```text
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

```text
Request → Controller → Service → Repository → PostgreSQL
                         ↓
                   DTO / Exception
                         ↓
              GlobalExceptionHandler (400/404)
```

---

## Troubleshooting

**`mvn: command not found`**  
ใช้ `./run.sh` แทน หรือ:

```bash
export PATH="$(pwd)/.tools/apache-maven-3.9.9/bin:$PATH"
```

หรือติดตั้งทั้งเครื่อง: `brew install maven`

**App ต่อ DB ไม่ได้**  
ตรวจว่า Postgres ขึ้นแล้ว: `docker compose ps`

**หยุดแอป**  
`Ctrl + C` ในเทอร์มินัลที่รัน `./run.sh`
