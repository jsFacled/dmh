FROM openjdk:17-jdk-slim
COPY target/msvc-users-0.0.1-SNAPSHOT.jar /app/msvc-users.jar
WORKDIR /app
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "msvc-users.jar"]
