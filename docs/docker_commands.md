# Essential Docker Commands

## 1. Docker Installation and Version
- Check Docker version:
   
  docker --version
   
- Check detailed Docker information:
   
  docker info
   

## 2. Working with Docker Images
- List all available images:
   
  docker images
   
- Pull an image from Docker Hub:
   
  docker pull <image_name>
   
- Remove an image:
   
  docker rmi <image_id>
   

## 3. Working with Docker Containers
- List running containers:
   
  docker ps
   
- List all containers (including stopped ones):
   
  docker ps -a
   
- Run a container interactively:
   
  docker run -it <image_name> /bin/bash
   
- Run a container in detached mode:
   
  docker run -d <image_name>
   
- Stop a running container:
   
  docker stop <container_id>
   
- Start a stopped container:
   
  docker start <container_id>
   
- Restart a container:
   
  docker restart <container_id>
   
- Remove a container:
   
  docker rm <container_id>
   

## 4. Docker Logs and Monitoring
- View container logs:
   
  docker logs <container_id>
   
- View running processes inside a container:
   
  docker top <container_id>
   
- View real-time container resource usage:
   
  docker stats
   
- Inspect detailed container information:
   
  docker inspect <container_id>
   

## 5. Docker Networking
- List all networks:
   
  docker network ls
   
- Create a network:
   
  docker network create <network_name>
   
- Connect a container to a network:
   
  docker network connect <network_name> <container_id>
   
- Disconnect a container from a network:
   
  docker network disconnect <network_name> <container_id>
   

## 6. Docker Volumes and Storage
- List all volumes:
   
  docker volume ls
   
- Create a volume:
   
  docker volume create <volume_name>
   
- Remove a volume:
   
  docker volume rm <volume_name>
   
- Use a volume in a container:
   
  docker run -v <volume_name>:/path/in/container <image_name>
   

## 7. Docker Compose
- Start services defined in `docker-compose.yml`:
   
  docker-compose up -d
   
- Stop services:
   
  docker-compose down
   
- Restart services:
   
  docker-compose restart
   
- View logs of services:
   
  docker-compose logs
   

## 8. Docker Build and Tagging
- Build an image from a Dockerfile:
   
  docker build -t <image_name> .
   
- Tag an image:
   
  docker tag <image_name> <repository_name>:<tag>
   
- Push an image to Docker Hub:
   
  docker push <repository_name>:<tag>
   
- Remove dangling images:
   
  docker image prune
   

## 9. Docker System Cleanup
- Remove all stopped containers:
   
  docker container prune
   
- Remove all unused networks:
   
  docker network prune
   
- Remove all unused images:
   
  docker image prune -a
   
- Remove all unused volumes:
   
  docker volume prune
   
- Remove all unused resources:
   
  docker system prune -a
   

These commands will help you efficiently manage Docker containers, images, networks, and volumes.
