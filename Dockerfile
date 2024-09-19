FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/
RUN mv /app/event-manager-backend-*.jar /app/event-manager-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "event-manager-backend.jar"]
