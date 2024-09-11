FROM openjdk:17-jdk-slim
COPY target/msvc-cards-0.0.1-SNAPSHOT.jar /app/msvc-cards.jar
WORKDIR /app
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "msvc-cards.jar"]
