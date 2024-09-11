FROM openjdk:17-jdk-slim
COPY target/msvc-configServer-0.0.1-SNAPSHOT.jar /app/msvc-configserver.jar
WORKDIR /app
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "msvc-configserver.jar"]
