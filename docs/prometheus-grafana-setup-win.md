Below is a beginner-friendly setup for **Windows (without Docker)** where:

* **Prometheus** is installed natively.
* **Grafana** is installed natively.
* Two **Spring Boot microservices** run on:

  * `http://localhost:8081`
  * `http://localhost:8082`
* Both applications expose Micrometer metrics to Prometheus.

---

# Recommended Directory Structure

Avoid installing under `Program Files` because Windows permissions can sometimes cause issues.

```
C:\
 ├── tools\
 │    ├── prometheus\
 │    └── grafana\
 │
 ├── apps\
 │    ├── service-one\
 │    └── service-two\
 │
 └── monitoring\
```

This keeps monitoring tools separate from applications.

---

# Step 1 Install Prometheus

Download Prometheus Windows ZIP.

Extract to

```
C:\tools\prometheus
```

It should contain

```
prometheus.exe
promtool.exe
prometheus.yml
console_libraries
consoles
```

---

# Step 2 Install Grafana

Download Grafana Windows ZIP or Installer.

Install/extract to

```
C:\tools\grafana
```

---

# Step 3 Set Environment Variables

Open **Command Prompt as Administrator**

### Add Prometheus

```
setx PATH "%PATH%;C:\tools\prometheus"
```

### Add Grafana

```
setx PATH "%PATH%;C:\tools\grafana\bin"
```

Close the command prompt.

Open a **new Command Prompt**.

Verify

```
prometheus --version
```

and

```
grafana-server -v
```

Both should print version information.

---

# Step 4 Enable Spring Boot Metrics

Both services require

### Maven

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

---

application.yml

## Service 1 (8081)

```yaml
server:
  port: 8081

management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
  endpoint:
    health:
      show-details: always
  prometheus:
    metrics:
      export:
        enabled: true
```

---

## Service 2 (8082)

```yaml
server:
  port: 8082

management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
  endpoint:
    health:
      show-details: always
  prometheus:
    metrics:
      export:
        enabled: true
```

---

# Step 5 Verify Metrics

Start both services.

Open browser

Service 1

```
http://localhost:8081/actuator/prometheus
```

Service 2

```
http://localhost:8082/actuator/prometheus
```

You should see thousands of metrics.

---

# Step 6 Configure Prometheus

Edit

```
C:\tools\prometheus\prometheus.yml
```

Replace with

```yaml
global:
  scrape_interval: 15s

scrape_configs:

  - job_name: 'service-one'

    metrics_path: /actuator/prometheus

    static_configs:
      - targets:
          - localhost:8081

  - job_name: 'service-two'

    metrics_path: /actuator/prometheus

    static_configs:
      - targets:
          - localhost:8082
```

---

# Step 7 Start Prometheus

Open Command Prompt

```
cd C:\tools\prometheus
```

Run

```
prometheus.exe --config.file=prometheus.yml
```

You should see

```
Server is ready
```

Prometheus UI

```
http://localhost:9090
```

---

# Step 8 Verify Targets

Open

```
http://localhost:9090/targets
```

Expected

```
service-one   UP

service-two   UP
```

---

# Step 9 Test Queries

Inside Prometheus

```
jvm_memory_used_bytes
```

or

```
system_cpu_usage
```

or

```
process_uptime_seconds
```

---

# Step 10 Start Grafana

Open another Command Prompt

```
cd C:\tools\grafana\bin
```

Run

```
grafana-server.exe
```

Grafana

```
http://localhost:3000
```

Login

```
Username

admin

Password

admin
```

You will be prompted to change the password.

---

# Step 11 Add Prometheus Data Source

In Grafana:

1. Connections → Data Sources
2. Add data source
3. Select **Prometheus**
4. URL:

```
http://localhost:9090
```

5. Click **Save & Test**

Expected:

```
Data source is working
```

---

# Step 12 Create Dashboard

Create a new dashboard and add panels with queries such as:

| Panel         | PromQL                               |
| ------------- | ------------------------------------ |
| CPU Usage     | `system_cpu_usage`                   |
| JVM Heap      | `jvm_memory_used_bytes{area="heap"}` |
| Uptime        | `process_uptime_seconds`             |
| HTTP Requests | `http_server_requests_seconds_count` |
| Threads       | `jvm_threads_live_threads`           |

---

# Running Everything

Open four Command Prompt windows:

**Window 1**

```
cd C:\apps\service-one
mvn spring-boot:run
```

**Window 2**

```
cd C:\apps\service-two
mvn spring-boot:run
```

**Window 3**

```
cd C:\tools\prometheus
prometheus.exe --config.file=prometheus.yml
```

**Window 4**

```
cd C:\tools\grafana\bin
grafana-server.exe
```

Access:

* Service 1 metrics: `http://localhost:8081/actuator/prometheus`
* Service 2 metrics: `http://localhost:8082/actuator/prometheus`
* Prometheus: `http://localhost:9090`
* Grafana: `http://localhost:3000`


