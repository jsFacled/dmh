FROM openjdk:17-jdk-slim
COPY target/msvc-gateway-0.0.1-SNAPSHOT.jar /app/msvc-gateway.jar
WORKDIR /app
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "msvc-gateway.jar"]
