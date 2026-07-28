# greetings-spring-boot

2-day Spring Boot crash course — Java 21 + Spring Boot 3.5

## Prerequisites

- JDK 21 (repo มี Temurin ชั่วคราวที่ `.jdks/` หรือติดตั้งเอง: `brew install openjdk@21`)
- Maven 3.9+ (มีที่ `.tools/apache-maven-3.9.9/` หรือ `brew install maven`)

## Run

```bash
# จาก root ของ repo
export JAVA_HOME="$(pwd)/.jdks/jdk-21.0.7+6/Contents/Home"
export PATH="$JAVA_HOME/bin:$(pwd)/.tools/apache-maven-3.9.9/bin:$PATH"

mvn spring-boot:run
```

แล้วลอง:

```bash
curl "http://localhost:8080/api/v1/greetings"
curl "http://localhost:8080/api/v1/greetings?name=Wachayathorn"
```

## Project layout

```
src/main/java/com/wachayathorn/greetings/
├── GreetingsApplication.java          # entry point (@SpringBootApplication)
└── greeting/
    ├── GreetingController.java        # HTTP layer  (~ Gin handler)
    ├── GreetingService.java           # business logic (~ NestJS provider)
    └── GreetingResponse.java          # response DTO (Java record)
```

แผนเรียนเต็ม: [LEARNING_PLAN.md](./LEARNING_PLAN.md)
