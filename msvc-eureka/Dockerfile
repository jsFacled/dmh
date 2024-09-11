FROM openjdk:17-jdk-slim
COPY target/msvc-eureka-0.0.1-SNAPSHOT.jar /app/msvc-eureka.jar
WORKDIR /app
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "msvc-eureka.jar"]
