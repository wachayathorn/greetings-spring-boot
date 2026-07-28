# Spring Boot 2-Day Crash Course

Goal: onboard ได้บนทีม Spring Boot ภายใน 2 วัน  
Stack: **Java 21 + Spring Boot 3.x + Maven**  
Repo: `greetings-spring-boot` — โปรเจกต์จริง เรียนไปทำไป

> คุณมี backend แข็งแล้ว (Go/NestJS/Hexagonal) — โฟกัส map ความรู้เดิม → Spring ไม่ใช่สอน CS ใหม่

---

## Day 1 — พูดภาษา Java/Spring ได้

### Block 1 — Java ที่ใช้จริง (~2 ชม.)
- [ ] class / interface / record
- [ ] Optional, Stream, exception
- [ ] Lombok (`@Data`, `@RequiredArgsConstructor`) — อ่านโค้ดทีมเป็น
- [ ] package structure แบบ Spring

### Block 2 — Spring Boot core (~3 ชม.)
- [ ] starter + auto-configuration คืออะไร
- [ ] DI: constructor injection (ห้าม field `@Autowired` ในโค้ดใหม่)
- [ ] `@RestController` / `@Service` / `@Component`
- [ ] `application.yml` + profiles (`local`, `dev`)

### Block 3 — REST API มาตรฐาน (~3 ชม.)
- [ ] Controller → Service layer
- [ ] Request/Response DTO (อย่า expose entity ตรง ๆ)
- [ ] `@Valid` + Bean Validation
- [ ] `@ControllerAdvice` global exception handler
- [ ] HTTP status ที่ถูกต้อง (200/201/400/404/500)

**Day 1 deliverable:** `GET/POST /api/v1/greetings` ทำงาน + validation + error response สวย

---

## Day 2 — Persistence + Security + Test แล้วส่งงานได้

### Block 4 — JPA + DB (~3 ชม.)
- [ ] Entity + Spring Data JPA repository
- [ ] `@Transactional` พื้นฐาน
- [ ] Flyway migration
- [ ] docker-compose (PostgreSQL)

### Block 5 — Security พื้นฐาน (~2 ชม.)
- [ ] Spring Security filter chain
- [ ] JWT หรือ HTTP Basic (เลือกอย่างใดอย่างหนึ่งให้จบ)
- [ ] protect `/api/**`, เปิด `/actuator/health`

### Block 6 — Test + ship (~3 ชม.)
- [ ] JUnit 5 unit test (service)
- [ ] `@WebMvcTest` / MockMvc (controller)
- [ ] README รันได้: `./mvnw spring-boot:run`
- [ ] springdoc-openapi (Swagger UI)

**Day 2 deliverable:** Greetings API + DB + auth พื้นฐาน + tests เขียว + README

---

## Map จากที่คุณรู้แล้ว

| คุณรู้จัก | ใน Spring Boot |
|---|---|
| Gin / Fiber handler | `@RestController` |
| NestJS provider / DI | `@Service` + constructor injection |
| Hexagonal port | interface + `@Component` impl |
| Liquibase / Atlas | Flyway |
| Swagger | springdoc-openapi |
| go test / testify | JUnit 5 + MockMvc |

---

## ตัดออกจาก 2 วันนี้ (เรียนตอนเข้าทีม)
- WebFlux / reactive ลึก
- Kafka / PubSub ลึก
- custom starter / AOP ซับซ้อน
- multi-service mesh

---

## Checklist พร้อมทำงาน

- [ ] สร้าง/รัน Spring Boot project เองได้
- [ ] เขียน endpoint + service + repository ได้
- [ ] อ่าน bean / DI / config ได้
- [ ] map entity ↔ table ได้
- [ ] validation + error handling มาตรฐานได้
- [ ] รัน test และอธิบาย request flow ได้

---

## Session log

| เมื่อไหร่ | ทำอะไร | ผล |
|---|---|---|
| Day 0 setup | Temurin 21 + Maven + scaffold Hello API | scaffold เสร็จ — รันด้วย `./run.sh` จากเทอร์มินัลเครื่องคุณ |
| MVP CRUD | Greetings CRUD + Postgres + Flyway + Swagger + tests ทุกชั้น | Controller/Service/Repository + docker-compose |
