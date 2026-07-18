# Spring Cloud Gateway Rate Limiting POC with JMeter

This POC demonstrates:

* Requests within the limit → HTTP 200
* Requests exceeding the limit → HTTP 429 (Too Many Requests)
* Load testing using JMeter
* Monitoring success and failure scenarios

---

# Architecture

```text
+----------+        +-------------------+       +----------------+
|  JMeter  | -----> | Spring Cloud      | ----> | Backend Service|
|          |        | Gateway           |       | Greeting API   |
+----------+        +-------------------+       +----------------+
                         |
                         |
                    Redis Rate
                     Limiter
```

Spring Cloud Gateway uses Redis-based token bucket algorithm.

---

# Step 1: Run Redis

Docker:

```bash
docker run -d --name redis -p 6379:6379 redis
```

Verify:

```bash
docker ps
```

---

# Step 2: Backend Service (Create this as a seperate service)

### Greeting Controller

```java
@RestController
@RequestMapping("/api")
public class GreetingController {

    @GetMapping("/greet")
    public String greet() {

        return "Hello from backend @ "
                + LocalDateTime.now();
    }
}
```

Run on:

```properties
server.port=8081
```

---

# Step 3: Gateway Dependencies

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    </dependency>

</dependencies>
```

---

# Step 4: Gateway Configuration

```yaml
server:
  port: 8080

spring:
  redis:
    host: localhost
    port: 6379

  cloud:
    gateway:
      routes:
        - id: greeting-service
          uri: http://localhost:8081

          predicates:
            - Path=/api/**

          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 5
                redis-rate-limiter.burstCapacity: 10
                key-resolver: "#{@ipKeyResolver}"
```

Meaning:

| Property         | Meaning            |
| ---------------- | ------------------ |
| replenishRate=5  | 5 tokens/sec added |
| burstCapacity=10 | max burst = 10     |
| keyResolver      | limit per client   |

---

# Step 5: Key Resolver

Limit based on IP.

```java
@Configuration
public class GatewayConfig {

    @Bean
    public KeyResolver ipKeyResolver() {

        return exchange ->
                Mono.just(
                    exchange
                    .getRequest()
                    .getRemoteAddress()
                    .getAddress()
                    .getHostAddress()
                );
    }
}
```

---

# Step 6: Gateway Main Class

```java
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                GatewayApplication.class,
                args);
    }
}
```

---

# Testing Manually

## First few requests

```bash
curl http://localhost:8080/api/greet
```

Response:

```text
Hello from backend
```

HTTP:

```text
200 OK
```

---

## Flood Requests

Linux

```bash
for i in {1..30}
do
 curl http://localhost:8080/api/greet
done
```

After limit crossed:

```text
429 Too Many Requests
```

---

# JMeter Test Plan

## Thread Group

```text
Users            : 50
Ramp-up          : 1 second
Loop Count       : 5
```

Total Requests:

```text
250 requests
```

---

## HTTP Request

```text
Protocol : http
Server   : localhost
Port     : 8080
Method   : GET
Path     : /api/greet
```

---

## View Results Tree

Add:

```text
Listener
  -> View Results Tree
```

Observe:

```text
200
200
200
429
429
429
```

---

# Success Case

### Configuration

```yaml
replenishRate: 100
burstCapacity: 100
```

### JMeter

```text
Users: 20
Loops: 1
```

Results:

```text
200 = 20
429 = 0
```

Success %

```text
100%
```

---

# Failure Case

### Configuration

```yaml
replenishRate: 2
burstCapacity: 5
```

### JMeter

```text
Users: 100
Loops: 2
```

Total:

```text
200 Requests
```

Expected:

```text
200 OK            ~5-10
429 Too Many      ~190+
```

---

# Assertions

Add Response Assertion.

Success:

```text
Response Code = 200
```

Failure:

```text
Response Code = 429
```

---

# Useful Headers Returned by Gateway

When rate limiting triggers:

```text
X-RateLimit-Remaining
X-RateLimit-Replenish-Rate
X-RateLimit-Burst-Capacity
```

Example:

```text
X-RateLimit-Remaining: 0
```

Useful for monitoring.

---

# Realistic Demo Scenario

Configure:

```yaml
replenishRate: 10
burstCapacity: 20
```

Run JMeter:

```text
100 Users
Ramp-up 5 sec
Loop 10
```

Expected:

| Response | Count                              |
| -------- | ---------------------------------- |
| 200      | Initial burst + replenished tokens |
| 429      | Excess traffic                     |

This provides a demonstration of API Gateway protection under load and is commonly used in Spring Cloud Gateway.
