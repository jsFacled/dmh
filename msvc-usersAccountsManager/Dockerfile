FROM openjdk:17-jdk-slim
COPY target/msvc-usersAccountsManager-0.0.1-SNAPSHOT.jar /app/msvc-usersAccountsManager.jar
WORKDIR /app
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "msvc-usersAccountsManager.jar"]
