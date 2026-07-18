## Reactive Programming and Spring WebFlux – Short Note 

### What is Reactive Programming?

Reactive Programming is a programming style designed to handle **asynchronous and non-blocking operations** efficiently. Instead of waiting for a task to finish, an application can continue doing other work and react when the result becomes available.

Think of ordering food in a restaurant:

* **Blocking (Traditional)**: You stand at the counter and wait until your food is ready.
* **Non-Blocking (Reactive)**: You place the order, get a token, and continue doing other things. When the food is ready, you are notified.

This approach helps applications handle thousands of concurrent requests with fewer resources.

---

### Key Characteristics

1. **Asynchronous**

   * Tasks run independently without blocking the main thread.

2. **Non-Blocking**

   * Threads are not kept idle while waiting for I/O operations.

3. **Event-Driven**

   * Applications react to events such as incoming requests, database responses, or messages.

4. **Scalable**

   * Better resource utilization for high-traffic applications.

---

### Reactive Streams Concepts

Reactive Programming is based on four concepts:

* **Publisher** – Produces data.
* **Subscriber** – Consumes data.
* **Subscription** – Connects publisher and subscriber.
* **Backpressure** – Prevents consumers from being overwhelmed by too much data.

---

### What is Spring WebFlux?

Spring WebFlux is Spring's reactive web framework introduced in Spring 5. It is built on top of the Reactive Streams specification and uses the **Project Reactor** library.

WebFlux enables developers to build:

* Reactive REST APIs
* Streaming applications
* High-concurrency services
* Event-driven microservices

---

### Core Types in WebFlux

#### Mono<T>

Represents **0 or 1 result**.

```java
Mono<String> mono = Mono.just("Hello WebFlux");
```

Examples:

* Find user by ID
* Fetch a single record

---

#### Flux<T>

Represents **0 to N results**.

```java
Flux<String> flux = Flux.just("Java", "Spring", "WebFlux");
```

Examples:

* List of users
* Stream of events
* Multiple database records

---

### Simple Reactive Controller

```java
@RestController
@RequestMapping("/api")
public class GreetingController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello Reactive World");
    }

    @GetMapping("/languages")
    public Flux<String> languages() {
        return Flux.just("Java", "Spring", "WebFlux");
    }
}
```

---

### Traditional Spring MVC vs Spring WebFlux

| Feature            | Spring MVC        | Spring WebFlux           |
| ------------------ | ----------------- | ------------------------ |
| Processing Model   | Blocking          | Non-Blocking             |
| Thread per Request | Yes               | No                       |
| Scalability        | Moderate          | High                     |
| Return Types       | Object            | Mono / Flux              |
| Suitable For       | Typical CRUD Apps | High-Concurrency Systems |

---

### When to Use WebFlux?

Use WebFlux when:

Building high-throughput APIs

Calling multiple external services

Working with streaming data

Developing event-driven microservices

Handling thousands of concurrent connections

Avoid WebFlux when:

Application is simple CRUD

Team is unfamiliar with reactive concepts

Blocking libraries must be used everywhere

---

### Benefits

* Better scalability
* Lower memory consumption
* Efficient thread utilization
* Improved performance under heavy load
* Ideal for cloud-native and microservice architectures

---

### Quick Summary

**Reactive Programming** is about processing data asynchronously and non-blockingly.

**Spring WebFlux** is Spring's reactive framework that uses:

* **Mono** → Single result
* **Flux** → Multiple results
* **Project Reactor** → Reactive library
* **Event Loop Model** → Efficient thread usage


## Spring WebFlux WebClient – Short Note 

### What is WebClient?

**WebClient** is Spring WebFlux's modern HTTP client used to call REST APIs in a **non-blocking and reactive** manner.

It replaces the older **RestTemplate**, which is blocking and is no longer recommended for new development.

---

## Why WebClient?

Suppose your application needs to call another microservice.

### Traditional RestTemplate (Blocking)

```text
Request → Wait → Response
```

The thread remains occupied until the response arrives.

### WebClient (Non-Blocking)

```text
Request Sent → Continue Processing
                  ↓
            Response Arrives
                  ↓
             Process Result
```

The thread is free to handle other requests while waiting for the response.

---

## Add Dependency

Spring Boot WebFlux starter includes WebClient.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

---

## Configure WebClient Bean

```java
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }
}
```

---

## Calling an API Returning One Object

### Service

```java
@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<User> getUser(Long id) {

        return webClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(User.class);
    }
}
```

### Flow

```text
WebClient
   ↓
HTTP GET
   ↓
Response JSON
   ↓
Mono<User>
```

---

## Calling an API Returning Multiple Objects

```java
public Flux<Post> getPosts() {

    return webClient.get()
            .uri("/posts")
            .retrieve()
            .bodyToFlux(Post.class);
}
```

Returns:

```java
Flux<Post>
```

because multiple records are expected.

---

## Controller Example

```java
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users/{id}")
    public Mono<User> getUser(@PathVariable Long id) {
        return service.getUser(id);
    }
}
```

---

## POST Request Example

```java
public Mono<User> createUser(User user) {

    return webClient.post()
            .uri("/users")
            .bodyValue(user)
            .retrieve()
            .bodyToMono(User.class);
}
```

---

## Error Handling

```java
public Mono<User> getUser(Long id) {

    return webClient.get()
            .uri("/users/{id}", id)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response -> Mono.error(
                    new RuntimeException("User not found"))
            )
            .bodyToMono(User.class);
}
```

---

## Reactive Operators

### Transform Data

```java
service.getUser(1L)
       .map(user -> user.getName().toUpperCase());
```

### Filter Data

```java
service.getPosts()
       .filter(post -> post.getId() > 10);
```

### Combine Calls

```java
Mono<User> user = service.getUser(1L);
Mono<Account> account = accountService.getAccount(1L);

return Mono.zip(user, account);
```

---

## Beginner-Friendly Analogy

Imagine ordering food through a mobile app:

1. Place order (send HTTP request).
2. Continue chatting with friends (thread is free).
3. Notification arrives (response received).
4. Collect food (process result).

That's exactly how **WebClient** works.

---

## RestTemplate vs WebClient

| Feature               | RestTemplate | WebClient |
| --------------------- | ------------ | --------- |
| Blocking              | Yes          | No        |
| Reactive Support      | No           | Yes       |
| Mono/Flux             | No           | Yes       |
| Scalability           | Moderate     | High      |
| Recommended by Spring | No           | Yes       |

---

## Quick Summary

* **WebClient** is the reactive HTTP client in Spring WebFlux.
* Supports **GET, POST, PUT, DELETE** operations.
* Returns:

  * **Mono<T>** → One result
  * **Flux<T>** → Multiple results
* Non-blocking and asynchronous.
* Preferred over RestTemplate for new applications.
* Excellent for microservices that make many outbound REST calls.


