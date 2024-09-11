FROM openjdk:17-jdk-slim
COPY target/msvc-transactions-0.0.1-SNAPSHOT.jar /app/msvc-transactions.jar
WORKDIR /app
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "msvc-transactions.jar"]
