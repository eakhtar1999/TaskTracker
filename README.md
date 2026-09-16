# Task Tracker API

CRUD REST API for tracking tasks — Spring Boot + JdbcTemplate + hand-written
RowMapper (no Spring Data JPA / Hibernate).

## Stack
- Java 21, Spring Boot 3.3.4
- `spring-boot-starter-web`, `spring-boot-starter-jdbc`, `spring-boot-starter-validation`
- H2 in-memory DB
- JUnit 5, Mockito, MockMvc

## Run
\`\`\`bash
mvn spring-boot:run
\`\`\`
App on `http://localhost:8080`.

## Test
\`\`\`bash
mvn test
\`\`\`
