# Migrating Spring Cloud Applications to Kubernetes-Native

> A practical guide covering strategy, steps, dos, and don'ts for teams moving from Spring Cloud's service abstractions to Kubernetes-native equivalents.

---

## Why Migrate?

Spring Cloud was designed to bring distributed-systems patterns (service discovery, config management, circuit breaking, load balancing) to JVM apps **before** Kubernetes was mainstream. On Kubernetes, the platform itself provides many of these capabilities natively — continuing to use Spring Cloud abstractions on top leads to duplication, added complexity, and operational overhead.

---

## Mapping: Spring Cloud → Kubernetes Native

| Spring Cloud Feature | Kubernetes Native Equivalent |
|---|---|
| Eureka / Consul (Service Discovery) | Kubernetes DNS + Services |
| Spring Cloud Config Server | ConfigMaps + Secrets + Helm values |
| Ribbon / Spring Cloud LoadBalancer | kube-proxy + Service (ClusterIP) |
| Zuul / Spring Cloud Gateway | Ingress Controller (Nginx, Traefik) or Gateway API |
| Hystrix / Resilience4j | Istio / Linkerd (service mesh) or keep Resilience4j |
| Spring Cloud Sleuth + Zipkin | OpenTelemetry + Jaeger / Tempo |
| Spring Cloud Bus (config refresh) | Reloader / Stakater or Rolling Deployments |
| Spring Cloud Bootstrap context | Spring Boot `application.yml` + env vars |

---

## Pre-Migration Checklist

Before writing a single YAML file:

- [ ] Inventory all Spring Cloud dependencies across services
- [ ] Identify which services use Eureka, Config Server, Gateway, Sleuth
- [ ] Confirm Kubernetes cluster version and available add-ons (Ingress, CNI, cert-manager)
- [ ] Decide on service mesh: none, Istio, or Linkerd
- [ ] Establish observability stack (Prometheus, Grafana, Jaeger/Tempo)
- [ ] Plan secret management strategy (Kubernetes Secrets, Vault, External Secrets Operator)
- [ ] Define a rollback plan per service

---

## Migration Steps

### Step 1 — Remove Spring Cloud Bootstrap Context

Spring Cloud Bootstrap (`bootstrap.yml`, `spring-cloud-starter-bootstrap`) initializes a parent context before the application context. On Kubernetes this is unnecessary.

**Action:**
- Remove `spring-cloud-starter-bootstrap` dependency from `pom.xml` / `build.gradle`
- Rename `bootstrap.yml` → `application.yml` (or merge contents)
- Switch to Spring Boot's native `spring.config.import` if you still need external config

```xml
<!-- Remove this -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```

---

### Step 2 — Replace Spring Cloud Config Server with ConfigMaps and Secrets

**Action:**
- Export each environment's config properties into `ConfigMap` YAML files
- Store sensitive values (passwords, API keys) in Kubernetes `Secret` objects
- Mount ConfigMaps as environment variables or volume files in Deployment specs

```yaml
# configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: order-service-config
  namespace: production
data:
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/orders
  APP_FEATURE_FLAG_ENABLED: "true"
```

```yaml
# deployment.yaml (envFrom pattern)
envFrom:
  - configMapRef:
      name: order-service-config
  - secretRef:
      name: order-service-secrets
```

**For dynamic config refresh** — use [Stakater Reloader](https://github.com/stakater/Reloader) to automatically roll pods when a ConfigMap or Secret changes, instead of Spring Cloud Bus + `/actuator/refresh`.

---

### Step 3 — Replace Eureka / Consul with Kubernetes DNS

Kubernetes provides DNS-based service discovery out of the box. Every `Service` is reachable at:

```
<service-name>.<namespace>.svc.cluster.local
```

**Action:**
- Remove `spring-cloud-starter-netflix-eureka-client` and `@EnableEurekaClient`
- Remove `spring-cloud-starter-consul-discovery`
- Replace service URLs hardcoded with Eureka names with Kubernetes Service DNS names

```yaml
# application.yml — before (Eureka)
inventory-service:
  url: http://inventory-service  # resolved via Eureka

# application.yml — after (K8s DNS)
inventory-service:
  url: http://inventory-service.production.svc.cluster.local
```

Define a `ClusterIP` Service for each microservice:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: inventory-service
  namespace: production
spec:
  selector:
    app: inventory-service
  ports:
    - port: 8080
      targetPort: 8080
```

---

### Step 4 — Replace Ribbon / Spring Cloud LoadBalancer with Kubernetes Services

`kube-proxy` handles L4 load balancing across pod replicas automatically via `ClusterIP` Services. You do not need client-side load balancing.

**Action:**
- Remove `spring-cloud-starter-loadbalancer` (or Ribbon)
- Remove `@LoadBalanced` `RestTemplate` / `WebClient` beans
- Use plain `RestTemplate` or `WebClient` with Service DNS names

```java
// Before — @LoadBalanced (Ribbon/SCL)
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}

// After — plain bean, K8s Service does the load balancing
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

---

### Step 5 — Replace Zuul / Spring Cloud Gateway with Kubernetes Ingress or Gateway API

**Option A — Ingress Controller (simpler)**

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: api-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  ingressClassName: nginx
  rules:
    - host: api.example.com
      http:
        paths:
          - path: /orders
            pathType: Prefix
            backend:
              service:
                name: order-service
                port:
                  number: 8080
          - path: /inventory
            pathType: Prefix
            backend:
              service:
                name: inventory-service
                port:
                  number: 8080
```

**Option B — Kubernetes Gateway API (recommended for new clusters, k8s 1.24+)**

Use `HTTPRoute` resources with a `Gateway` object for fine-grained routing, header manipulation, and traffic splitting without annotations.

> **Note:** If you have complex routing logic (rate limiting, JWT validation, request transformation), keep Spring Cloud Gateway as a dedicated microservice deployed *inside* the cluster, but remove its service-discovery dependency on Eureka.

---

### Step 6 — Replace Hystrix with Resilience4j or a Service Mesh

Hystrix is in maintenance mode. Options:

**Keep Resilience4j** (recommended for circuit breaking at the code level):
- Already Spring Boot–native; no Spring Cloud dependency needed
- Configure via `application.yml` with `resilience4j.*` properties

**Use Istio / Linkerd** for mesh-level resilience:
- `DestinationRule` for circuit breaking
- `VirtualService` for retries, timeouts, traffic shifting
- Zero application-code changes required

```yaml
# Istio DestinationRule — circuit breaker example
apiVersion: networking.istio.io/v1alpha3
kind: DestinationRule
metadata:
  name: inventory-service
spec:
  host: inventory-service
  trafficPolicy:
    outlierDetection:
      consecutive5xxErrors: 5
      interval: 10s
      baseEjectionTime: 30s
```

---

### Step 7 — Replace Spring Cloud Sleuth with OpenTelemetry

Sleuth auto-instruments trace/span propagation. On Kubernetes the standard is OpenTelemetry.

**Action:**
- Remove `spring-cloud-starter-sleuth` and `spring-cloud-sleuth-zipkin`
- Add `micrometer-tracing-bridge-otel` + `opentelemetry-exporter-otlp`
- Deploy an OpenTelemetry Collector as a DaemonSet or Deployment
- Route traces to Jaeger, Grafana Tempo, or your preferred backend

```xml
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-tracing-bridge-otel</artifactId>
</dependency>
<dependency>
  <groupId>io.opentelemetry</groupId>
  <artifactId>opentelemetry-exporter-otlp</artifactId>
</dependency>
```

```yaml
# application.yml
management:
  tracing:
    sampling:
      probability: 1.0
  otlp:
    tracing:
      endpoint: http://otel-collector:4318/v1/traces
```

---

### Step 8 — Health Checks via Actuator → Liveness and Readiness Probes

Spring Boot Actuator's `/actuator/health/liveness` and `/actuator/health/readiness` map directly to Kubernetes probes.

```yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10

readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8080
  initialDelaySeconds: 20
  periodSeconds: 5
```

```yaml
# application.yml
management:
  health:
    livenessState:
      enabled: true
    readinessState:
      enabled: true
  endpoint:
    health:
      probes:
        enabled: true
```

---

### Step 9 — Containerize Correctly

**Use layered JARs or Buildpacks:**

```bash
# Cloud Native Buildpacks (no Dockerfile needed)
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=myrepo/order-service:1.0.0

# Or use layered JAR Dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/dependency/BOOT-INF/lib           ./BOOT-INF/lib
COPY target/dependency/META-INF               ./META-INF
COPY target/dependency/BOOT-INF/classes       ./BOOT-INF/classes
COPY target/dependency/org                    ./org
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
```

---

### Step 10 — Resource Requests and Limits

Always set JVM heap to respect container limits:

```yaml
resources:
  requests:
    memory: "512Mi"
    cpu: "250m"
  limits:
    memory: "1Gi"
    cpu: "1000m"

env:
  - name: JAVA_TOOL_OPTIONS
    value: "-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport"
```

---

## ✅ Dos

- **Do** use `spring.application.name` consistently — Kubernetes uses it for Service naming and log correlation.
- **Do** expose Actuator metrics (`/actuator/prometheus`) and scrape with Prometheus.
- **Do** use `Deployment` + `HorizontalPodAutoscaler` instead of manually scaling replicas.
- **Do** add `PodDisruptionBudget` for critical services to ensure availability during node drains.
- **Do** store secrets in an external secrets manager (HashiCorp Vault, AWS Secrets Manager) synced via External Secrets Operator — not raw Kubernetes Secrets checked into Git.
- **Do** use namespaces to isolate environments (dev / staging / production) within the same cluster.
- **Do** migrate one service at a time — strangler fig pattern, not big bang.
- **Do** keep Resilience4j for application-level circuit breaking even if you adopt a service mesh (defense in depth).
- **Do** pin container image tags to digests (`image@sha256:...`) in production.
- **Do** use `terminationGracePeriodSeconds` + graceful shutdown (`spring.lifecycle.timeout-per-shutdown-phase`) to prevent in-flight request drops during rolling deployments.

---

## ❌ Don'ts

- **Don't** run a Eureka Server inside Kubernetes — it's redundant and adds failure surface.
- **Don't** run a Spring Cloud Config Server unless you have a very specific requirement; ConfigMaps + Secrets cover 90% of cases.
- **Don't** use `@LoadBalanced` `RestTemplate` / `WebClient` — Kubernetes Services handle this; double load-balancing causes issues.
- **Don't** set JVM heap with `-Xmx` to a fixed size larger than the container memory limit — the JVM will be OOMKilled.
- **Don't** use `hostNetwork: true` or fixed `hostPort` — it breaks horizontal scaling.
- **Don't** store secrets in `application.yml` or Docker images — ever.
- **Don't** run as root inside containers — use `runAsNonRoot: true` in the security context.
- **Don't** skip readiness probes — traffic will be sent to pods that haven't finished startup.
- **Don't** set `initialDelaySeconds` too low for liveness probes — slow-starting Spring Boot apps will be killed in a restart loop.
- **Don't** migrate all services simultaneously — this makes rollback nearly impossible.
- **Don't** leave `spring.cloud.config.fail-fast=true` without a fallback — one Config Server blip will bring all your pods down.
- **Don't** ignore `SIGTERM` — make sure `server.shutdown=graceful` is set in `application.yml`.

---

## Recommended Spring Boot Properties Post-Migration

```yaml
spring:
  application:
    name: order-service
  lifecycle:
    timeout-per-shutdown-phase: 30s

server:
  shutdown: graceful
  port: 8080

management:
  endpoints:
    web:
      exposure:
        include: health, info, prometheus, metrics
  health:
    livenessState:
      enabled: true
    readinessState:
      enabled: true
  endpoint:
    health:
      probes:
        enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
  tracing:
    sampling:
      probability: 1.0
  otlp:
    tracing:
      endpoint: http://otel-collector:4318/v1/traces
```

---

## Rollback Strategy

Each migration step should be independently reversible:

1. Keep Spring Cloud Config Server running until **all** services have validated ConfigMap-based config in staging.
2. Run Eureka and Kubernetes DNS in parallel during transition using Spring profiles.
3. Use feature flags or Helm values to toggle between old and new config sources.
4. Tag images before and after migration; keep old images in the registry for at least one sprint.

---

## Summary Roadmap

```
Phase 1 — Foundation
  ├── Containerize with layered JARs / Buildpacks
  ├── Add liveness + readiness probes
  ├── Set resource requests/limits + JVM container flags
  └── Set up Prometheus + Grafana

Phase 2 — Config & Discovery
  ├── Migrate Config Server → ConfigMaps + Secrets
  ├── Remove Eureka → Kubernetes DNS
  └── Remove Ribbon → K8s Services

Phase 3 — Edge & Resilience
  ├── Replace Zuul/Gateway → Ingress or Gateway API
  ├── Replace Hystrix → Resilience4j or Istio
  └── Add PodDisruptionBudgets + HPA

Phase 4 — Observability
  ├── Replace Sleuth → OpenTelemetry
  ├── Centralize logs (Fluent Bit → Loki / Elasticsearch)
  └── Distributed traces → Grafana Tempo / Jaeger
```
