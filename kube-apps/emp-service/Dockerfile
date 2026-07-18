FROM eclipse-temurin:21.0.11_10-jdk-noble
WORKDIR /app/
COPY target/*.jar app.jar
EXPOSE 8181
ENV DB_HOST=localhost
ENV DB_PORT=3306
ENV DB_USER=root
ENV DB_PASSWORD=P@ssword
CMD java -jar app.jar

## CD to the location of Dockerfile
##docker build -t emp-service:1.0