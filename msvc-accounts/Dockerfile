FROM openjdk:17-jdk-slim
COPY target/msvc-accounts-0.0.1-SNAPSHOT.jar /app/msvc-accounts.jar
WORKDIR /app
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "msvc-accounts.jar"]
