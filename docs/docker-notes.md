# Docker Architecture and Important Commands – A Detailed Dissertation

## 1. Introduction to Docker

Docker is an open-source containerization platform that enables developers to package applications along with their dependencies into lightweight, portable units called **containers**.

Unlike traditional virtual machines, containers share the host operating system kernel, making them:

* Lightweight
* Fast to start
* Resource-efficient
* Portable across environments

Docker follows the principle:

> "Build Once, Run Anywhere."

---

# 2. Why Docker?

Before Docker, applications often faced the famous problem:

> "It works on my machine but not in production."

Reasons included:

* Different OS versions
* Different library versions
* Missing dependencies
* Environment inconsistencies

Docker solves this by packaging:

* Application code
* Runtime
* Libraries
* Dependencies
* Configuration

inside a container.

---

# 3. Docker Architecture

Docker uses a **Client-Server Architecture**.

```text
+--------------------+
|    Docker Client   |
| (docker commands)  |
+---------+----------+
          |
          | REST API
          |
+---------v----------+
|    Docker Daemon   |
|      dockerd       |
+----+---------+-----+
     |         |
     |         |
     v         v
 Images    Containers
     |
     v
 Docker Registry
 (Docker Hub)
```

---

# 4. Components of Docker Architecture

## 4.1 Docker Client

The Docker Client is the interface used by users.

Example:

```bash
docker run nginx
docker ps
docker build .
```

When you execute a command:

```bash
docker run nginx
```

the client sends a request to the Docker Daemon.

---

## 4.2 Docker Daemon (dockerd)

The Docker Daemon is the heart of Docker.

Responsibilities:

* Building images
* Running containers
* Managing networks
* Managing storage volumes
* Pulling images
* Managing registries

Daemon runs as a background service:

Linux:

```bash
systemctl status docker
```

Windows:

```powershell
Get-Service docker
```

---

## 4.3 Docker Registry

A registry stores Docker images.

Popular registries:

* Docker Hub
* Amazon Elastic Container Registry
* Google Container Registry
* GitHub Container Registry

Workflow:

```text
Developer
    |
docker push
    |
Registry
    |
docker pull
    |
Production Server
```

---

# 5. Docker Objects

Docker works with several important objects.

---

## 5.1 Images

An image is a read-only template.

Example:

```bash
nginx:latest
mysql:8
openjdk:21
```

Think of image as:

```text
Class -> Image
Object -> Container
```

List images:

```bash
docker images
```

Pull image:

```bash
docker pull nginx
```

---

## 5.2 Containers

A running instance of an image.

```bash
docker run nginx
```

Container lifecycle:

```text
Created
   |
Running
   |
Paused
   |
Stopped
   |
Deleted
```

---

## 5.3 Volumes

Volumes provide persistent storage.

Without volume:

```text
Container Deleted
      ↓
Data Lost
```

With volume:

```text
Container Deleted
      ↓
Data Remains
```

Example:

```bash
docker volume create mysql-data
```

Mount volume:

```bash
docker run -v mysql-data:/var/lib/mysql mysql
```

---

## 5.4 Networks

Docker provides networking between containers.

Types:

### Bridge

Default network

```bash
docker network ls
```

### Host

Container uses host network.

```bash
docker run --network host nginx
```

### None

No networking.

```bash
docker run --network none nginx
```

---

# 6. Internal Docker Architecture

When Docker starts a container:

```text
Docker Client
      |
Docker Daemon
      |
Container Runtime
      |
Linux Kernel
```

Docker internally uses:

### Namespaces

Provide isolation.

Types:

* PID
* Network
* Mount
* User
* IPC

Example:

Container A process list:

```text
PID 1 Java
PID 2 Thread
```

Container B cannot see them.

---

### Control Groups (cgroups)

Limit resources.

Example:

```bash
docker run \
--memory=512m \
--cpus=1 nginx
```

Impact:

* Prevents resource exhaustion
* Better multi-tenancy

---

### Union File System

Docker images are layered.

```text
Ubuntu Layer
     +
Java Layer
     +
Application Layer
```

Benefits:

* Smaller images
* Faster downloads
* Layer caching

---

# 7. Docker Workflow

## Step 1

Create Dockerfile

```dockerfile
FROM eclipse-temurin:21
COPY target/app.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
```

## Step 2

Build image

```bash
docker build -t myapp .
```

## Step 3

Run container

```bash
docker run -p 8080:8080 myapp
```

## Step 4

Push image

```bash
docker push username/myapp
```

---

# 8. Impact of Docker

## Development Impact

Before Docker:

```text
Developer Machine
      ≠
Test Environment
      ≠
Production
```

After Docker:

```text
Developer
      =
Testing
      =
Production
```

Benefits:

* Consistency
* Reproducibility
* Faster onboarding

---

## Deployment Impact

Traditional Deployment:

```text
OS
 ↓
Middleware
 ↓
Application
```

Docker Deployment:

```text
Container
   ↓
Host
```

Impact:

* Faster deployment
* Easier rollback
* Better portability

---

## Resource Utilization Impact

### Virtual Machine

```text
App
Guest OS
Hypervisor
Host OS
```

Memory Usage:

```text
2-4 GB each
```

### Docker

```text
App
Container
Host Kernel
```

Memory Usage:

```text
100-500 MB
```

Result:

* Higher density
* Lower cost

---

# 9. Important Docker Commands

## Image Commands

Pull image

```bash
docker pull nginx
```

List images

```bash
docker images
```

Remove image

```bash
docker rmi nginx
```

Build image

```bash
docker build -t myapp .
```

---

## Container Commands

Run container

```bash
docker run nginx
```

Run detached

```bash
docker run -d nginx
```

Run with port mapping

```bash
docker run -p 8080:80 nginx
```

List running containers

```bash
docker ps
```

List all containers

```bash
docker ps -a
```

Stop container

```bash
docker stop container-id
```

Start container

```bash
docker start container-id
```

Restart container

```bash
docker restart container-id
```

Delete container

```bash
docker rm container-id
```

---

## Inspection Commands

View logs

```bash
docker logs container-id
```

Follow logs

```bash
docker logs -f container-id
```

Inspect container

```bash
docker inspect container-id
```

View processes

```bash
docker top container-id
```

Container statistics

```bash
docker stats
```

---

## Execute Commands

Access container shell

```bash
docker exec -it container-id bash
```

For Alpine images:

```bash
docker exec -it container-id sh
```

---

## Volume Commands

Create volume

```bash
docker volume create myvol
```

List volumes

```bash
docker volume ls
```

Inspect volume

```bash
docker volume inspect myvol
```

Delete volume

```bash
docker volume rm myvol
```

---

## Network Commands

List networks

```bash
docker network ls
```

Create network

```bash
docker network create app-net
```

Inspect network

```bash
docker network inspect app-net
```

Delete network

```bash
docker network rm app-net
```

---

## Cleanup Commands

Remove stopped containers

```bash
docker container prune
```

Remove unused images

```bash
docker image prune
```

Remove everything unused

```bash
docker system prune -a
```

---

# 10. Docker vs Virtual Machines

| Feature        | Docker        | Virtual Machine |
| -------------- | ------------- | --------------- |
| Boot Time      | Seconds       | Minutes         |
| Size           | MBs           | GBs             |
| Performance    | Near Native   | Overhead        |
| Isolation      | Process Level | Hardware Level  |
| Resource Usage | Low           | High            |
| Scalability    | High          | Moderate        |

---

# 11. Docker in Modern Cloud-Native Architecture

Docker is the foundation for:

* Microservices
* DevOps
* CI/CD Pipelines
* Kubernetes
* OpenShift
* Cloud-Native Applications

Typical flow:

```text
Developer
   ↓
Git
   ↓
Jenkins/GitHub Actions
   ↓
Docker Build
   ↓
Registry
   ↓
Kubernetes/OpenShift
   ↓
Production
```

---

# 12. Conclusion

Docker revolutionized application deployment by introducing lightweight containers that package applications and dependencies together. Its architecture—comprising the Docker Client, Docker Daemon, Images, Containers, Registries, Volumes, and Networks—provides a consistent runtime environment across development, testing, and production.

The impact of Docker includes:

* Faster application delivery
* Improved portability
* Better resource utilization
* Simplified DevOps workflows
* Strong foundation for Kubernetes and cloud-native platforms

As a result, Docker has become one of the most important technologies in modern software development and deployment ecosystems.
