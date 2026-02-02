# api-gateway

Distributed API Gateway with Rate Limiting

This project is a Spring Cloud Gateway application that implements rate limiting backed by Redis and a token-bucket algorithm.

Quick start

1. Build: `./mvnw clean package`
2. Run: `./mvnw spring-boot:run`

Notes

- Java 17 is required (see `pom.xml`).
- Configure Redis connection in `src/main/resources/application.yaml`.
