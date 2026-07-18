FROM eclipse-temurin:21.0.11_10-jdk-noble
WORKDIR /app/
COPY target/*.jar app.jar
EXPOSE 8280
ENV EMP_SERVICE_URL=localhost:8181
CMD java -jar app.jar

## CD to the location of Dockerfile
##docker build -t emp-service:1.0