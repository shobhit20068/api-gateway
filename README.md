# 🚦 Distributed API Gateway with Rate Limiting & Resilience

A **production-grade API Gateway** built using **Spring Boot, Spring Cloud Gateway, Redis, and Resilience4j** that provides **distributed rate limiting**, **fault tolerance**, and **real-time observability**.

This project demonstrates how to protect backend services at scale while maintaining high throughput and system reliability.

---

## ✨ Key Features

### 🔐 Distributed Rate Limiting
- Token Bucket algorithm
- Redis-backed shared state
- Handles **5,000+ requests/second**
- Tiered limits per API key (Free / Premium)

### 🛑 Fault Tolerance
- Retry with exponential backoff
- Circuit Breaker using Resilience4j
- Graceful fallback with proper HTTP semantics (503)

### 🔄 Smart Request Routing
- Centralized routing via Spring Cloud Gateway
- Early request rejection to protect backends

### 📊 Observability & Metrics
- Micrometer + Spring Boot Actuator
- Prometheus metrics exposure
- Grafana dashboards for real-time monitoring

---

## 🧠 Architecture Overview

```
Client
  ↓
API Gateway (Spring Cloud Gateway)
  ├── Rate Limiting (Redis)
  ├── Retry
  ├── Circuit Breaker
  ├── Fallback
  └── Metrics
  ↓
Backend Services
```

- Gateway instances are **stateless**
- Redis provides **distributed coordination**
- Horizontally scalable behind a load balancer

---

## 🛠 Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java |
| Framework | Spring Boot |
| Gateway | Spring Cloud Gateway |
| Rate Limiting | Redis |
| Resilience | Resilience4j |
| Metrics | Micrometer |
| Monitoring | Prometheus, Grafana |
| Build Tool | Maven |
| Runtime | Netty (Reactive) |

---

## 🚀 How It Works

### 1️⃣ Rate Limiting
- Each API key maps to a Redis token bucket
- Requests consume tokens
- When tokens are exhausted → **HTTP 429 Too Many Requests**

### 2️⃣ Retry Strategy
- Handles transient backend failures
- Configurable retries with exponential backoff
- Executed **before** circuit breaker

### 3️⃣ Circuit Breaker
- Tracks failure rate and slow calls
- Opens on repeated failures
- Prevents cascading backend failures

### 4️⃣ Fallback
- Activated when circuit is open
- Returns **HTTP 503 Service Unavailable**
- Ensures consistent failure responses

---

## 📈 Metrics & Observability

### Exposed Metrics
- Request throughput
- HTTP status distribution (2xx / 4xx / 5xx)
- Circuit breaker state and failure rate
- Backend latency

### Actuator Endpoints

```
/actuator/health
/actuator/metrics
/actuator/prometheus
```

### Example Metric

```bash
GET /actuator/metrics/spring.cloud.gateway.requests
```

### 📊 Grafana Dashboards

Key panels included:
* Requests per second
* Rate limit violations (HTTP 429)
* Circuit breaker open/close events
* Backend latency trends

These dashboards convert system behavior into actionable metrics.

---

## ⚙️ Configuration Highlights

### Redis (Rate Limiting)

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
```

### Gateway Filters

```yaml
filters:
  - name: Retry
  - name: CircuitBreaker
```

### Resilience4j Circuit Breaker

```yaml
resilience4j:
  circuitbreaker:
    instances:
      backendService:
        slidingWindowSize: 6
        minimumNumberOfCalls: 3
        failureRateThreshold: 50
```

---

## 🧪 Testing

### Load Test

```bash
for i in {1..100}; do
  curl http://localhost:8081/test/endpoint
done
```

### Expected Behavior
* Rate limit exceeded → 429
* Backend failure → retries → circuit opens → 503
* Metrics reflect all state transitions

### 📊 Dashboards as Code

Grafana dashboards are exported as JSON and stored in the repository.
This enables:
- Version control
- Easy environment replication
- Zero-click dashboard restoration

