---
marp: true
theme: default
class: lead
paginate: true
backgroundColor: #fff

---
# Micrometer with Spring Boot
---

## 1. What is Micrometer?

**Micrometer** is the metrics instrumentation library used by Spring Boot to collect application metrics. It provides a **vendor-neutral API**, allowing you to instrument your application once and export metrics to monitoring systems such as Prometheus, Dynatrace, Datadog, New Relic, CloudWatch, and many others.

Think of Micrometer as the **SLF4J of metrics**—your application code uses the Micrometer API, while the actual monitoring backend can be changed through configuration.

---

## 2. Why Use Micrometer?

Without Micrometer:

* Each monitoring tool has its own API.
* Changing monitoring platforms requires code changes.
* Applications become tightly coupled to a specific vendor.

With Micrometer:

* Instrument your code once.
* Switch monitoring backends without changing application code.
* Integrates seamlessly with Spring Boot Actuator.

---

## 3. Micrometer Architecture

```
Spring Boot Application
        │
        │
Business Logic
        │
        ▼
 Micrometer API
(MeterRegistry, Counter,
 Timer, Gauge...)
        │
        ▼
Monitoring Registry
(Prometheus, Dynatrace,
Datadog, New Relic...)
        │
        ▼
Visualization Tool
(Grafana, Dynatrace UI,
Datadog Dashboard)

```

---

## 4. Micrometer in Spring Boot 4

Spring Boot 4 continues to use Micrometer as the default metrics library and integrates it with Spring Boot Actuator.

Key features include:

* Automatic metrics collection
* JVM metrics
* HTTP request metrics
* Database connection pool metrics
* Thread pool metrics
* Cache metrics
* Custom business metrics
* Observation API support for metrics and tracing

---

## 5. Core Components

### Meter

A **Meter** represents a measurable quantity.

Examples:

* Number of requests
* Response time
* Queue size
* Memory usage

---

### MeterRegistry

The `MeterRegistry` stores and manages all application metrics.

Example:

```java
@Autowired
private MeterRegistry registry;
```

All custom metrics are registered with the `MeterRegistry`.

---

## 6. Types of Metrics

### Counter

Counts events that only increase.

Examples:

* Orders placed
* Login attempts
* Emails sent

```java
Counter counter =
    registry.counter("orders.created");

counter.increment();
```

---

### Timer

Measures execution time and invocation count.

Examples:

* REST API response time
* Database query duration
* Service execution time

```java
Timer timer =
    registry.timer("payment.processing");

timer.record(() -> paymentService.pay());
```

---

### Gauge

Represents a value that can increase or decrease.

Examples:

* Active users
* Queue size
* Memory usage

```java
AtomicInteger queue = new AtomicInteger();

registry.gauge("queue.size", queue);
```

---

### Distribution Summary

Measures distributions of values.

Examples:

* Order amounts
* File sizes
* Payload sizes

```java
DistributionSummary summary =
    DistributionSummary.builder("order.amount")
            .register(registry);

summary.record(500);
```

---

### Long Task Timer

Measures operations that run for an extended period.

Examples:

* File upload
* Batch processing
* Report generation

---

## 7. Automatic Metrics

Spring Boot automatically collects metrics for:

### JVM

* Heap memory
* Non-heap memory
* Garbage collection
* Thread count
* Class loading

---

### HTTP Server

* Request count
* Request duration
* Status codes
* URI
* HTTP method

---

### System

* CPU usage
* Disk space
* Uptime
* Processor count

---

### Database

* Active connections
* Idle connections
* Connection timeout
* Maximum pool size

---

### Thread Pools

Executor metrics:

* Active threads
* Queue size
* Completed tasks

---

## 8. Metric Naming

Micrometer uses **dot notation** in code:

```
orders.created
payment.processing
queue.size
```

Prometheus converts these names to **snake_case**:

```
orders_created_total
payment_processing_seconds
queue_size
```

---

## 9. Tags

Tags add dimensions to metrics, making them easier to filter and aggregate.

Example:

```java
Counter.builder("orders.created")
       .tag("region", "India")
       .tag("payment", "UPI")
       .register(registry)
       .increment();
```

You can then analyze metrics by region or payment method.

---

## 10. Observation API

Spring Boot 4 uses the Micrometer Observation API to unify:

* Metrics
* Distributed tracing
* Logging correlation

One observation can produce:

* Metrics
* Trace spans
* Correlated log entries

This enables consistent observability across your application.

---

## 11. Best Practices

* Use meaningful metric names.
* Add low-cardinality tags (avoid user IDs or timestamps).
* Prefer `@Timed` for measuring method execution.
* Use `Counter` for event counts.
* Use `Gauge` for values that fluctuate.
* Monitor business metrics (e.g., orders processed) alongside technical metrics.
* Keep tag values consistent across services.

---

## 12. Typical Flow

```
REST Request
      │
      ▼
Controller
      │
      ▼
Business Service
      │
      ▼
Micrometer records metrics
      │
      ▼
MeterRegistry
      │
      ▼
Prometheus/Dynatrace
      │
      ▼
Grafana/Dynatrace Dashboards
```

---

## 13. Key Takeaways

* **Micrometer** is Spring Boot's standard metrics facade.
* It integrates seamlessly with **Spring Boot Actuator**.
* Metrics can be exported to multiple monitoring systems without changing application code.
* It supports both **automatic infrastructure metrics** and **custom business metrics**.
* In **Spring Boot 4**, Micrometer works with the **Observation API** to provide a unified approach to metrics, tracing, and log correlation, making it the foundation of modern application observability.

---
Spring Boot observability with **Micrometer** and **Prometheus** is the most common way to monitor application health and performance. Micrometer acts as the metrics API, while Prometheus stores and queries the metrics.

## Architecture

```
                    +----------------------+
                    |  Spring Boot App     |
                    |----------------------|
                    | Business Logic       |
                    | Spring Actuator      |
                    | Micrometer           |
                    +----------+-----------+
                               |
                    /actuator/prometheus
                               |
                               |
                     Prometheus Server
                               |
                    Pull Metrics Every 15s
                               |
                      Time Series Database
                               |
                           Grafana
                               |
                      Dashboards & Alerts
```

---

# Step 1: Add Dependencies

**pom.xml**

```xml
<dependencies>

    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Actuator -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Prometheus Registry -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>

</dependencies>
```

---

# Step 2: Configure Actuator

`application.yml`

```yaml
server:
  port: 8081

management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus,metrics

  endpoint:
    health:
      show-details: always

  prometheus:
    metrics:
      export:
        enabled: true
```

---

# Step 3: Start Application

Open

```
http://localhost:8081/actuator
```

You should see

```
health
metrics
prometheus
info
```

---

# Step 4: Prometheus Endpoint

```
http://localhost:8081/actuator/prometheus
```

Output

```
http_server_requests_seconds_count

jvm_memory_used_bytes

system_cpu_usage

process_uptime_seconds

jvm_gc_pause_seconds

disk_free_bytes
```

Thousands of metrics are available automatically.

---

# Step 5: Install Prometheus

Create

```
prometheus.yml
```

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: springboot

    metrics_path: /actuator/prometheus

    static_configs:
      - targets:
          - localhost:8081
```

Start Prometheus

```
prometheus.exe --config.file=prometheus.yml
```

Open

```
http://localhost:9090
```

---

# Step 6: Query Metrics

Example queries

```
jvm_memory_used_bytes

process_cpu_usage

system_cpu_usage

http_server_requests_seconds_count

http_server_requests_seconds_sum

http_server_requests_seconds_max

disk_free_bytes

jvm_threads_live_threads

executor_active_threads

tomcat_sessions_active_current_sessions
```

---

# Step 7: Create Custom Metrics

Inject `MeterRegistry`

```java
@RestController
public class OrderController {

    private final Counter counter;

    public OrderController(MeterRegistry registry) {
        counter = registry.counter("orders.created");
    }

    @GetMapping("/order")
    public String createOrder() {

        counter.increment();

        return "Order Created";
    }
}
```

Calling

```
GET /order
```

increments

```
orders_created_total
```

---

# Step 8: Gauge Example

```java
@Component
public class QueueService {

    private final AtomicInteger queueSize = new AtomicInteger();

    public QueueService(MeterRegistry registry) {
        registry.gauge("queue.size", queueSize);
    }

    public void addJob() {
        queueSize.incrementAndGet();
    }

    public void removeJob() {
        queueSize.decrementAndGet();
    }
}
```

Prometheus metric

```
queue_size
```

---

# Step 9: Timer Example

```java
@Service
public class PaymentService {

    @Timed(value = "payment.time")
    public void pay() throws InterruptedException {

        Thread.sleep(400);

    }
}
```

Produces

```
payment_time_seconds_count

payment_time_seconds_sum

payment_time_seconds_max
```

---

# Step 10: Distribution Summary

```java
DistributionSummary summary =
    DistributionSummary.builder("payment.amount")
        .register(registry);

summary.record(250);

summary.record(800);
```

Metrics

```
payment_amount_sum

payment_amount_count

payment_amount_max
```

---

# Step 11: Common Built-in Metrics

| Category | Example Metrics                   |
| -------- | --------------------------------- |
| JVM      | Heap memory, GC, threads          |
| CPU      | Process CPU, System CPU           |
| Disk     | Free disk space                   |
| HTTP     | Request count, latency, errors    |
| Logback  | Log events                        |
| Tomcat   | Active sessions, threads          |
| HikariCP | Active, idle, pending connections |

---

# Step 12: Visualize in Grafana

Add Prometheus as a data source in Grafana and create dashboards using metrics such as:

* Requests per second:

  ```
  rate(http_server_requests_seconds_count[1m])
  ```
* Average response time:

  ```
  rate(http_server_requests_seconds_sum[1m]) /
  rate(http_server_requests_seconds_count[1m])
  ```
* JVM heap usage:

  ```
  jvm_memory_used_bytes{area="heap"}
  ```
* CPU usage:

  ```
  process_cpu_usage
  ```

## Hands-on exercise

1. Create a Spring Boot application on port `8081`.
2. Add Actuator and Micrometer Prometheus dependencies.
3. Verify `http://localhost:8081/actuator/prometheus`.
4. Configure Prometheus to scrape the endpoint every 15 seconds.
5. Add a custom `Counter` (`orders.created`) and a `Timer` (`payment.time`).
6. Generate traffic by invoking the REST endpoints multiple times.
7. Query the metrics in Prometheus and build a simple Grafana dashboard showing:

   * Total HTTP requests
   * Request latency
   * JVM memory usage
   * CPU usage
   * Custom order count

This setup is suitable for local development and production, and forms the foundation of a complete observability stack alongside distributed tracing and centralized logging.
