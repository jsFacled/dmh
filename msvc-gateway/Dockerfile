# Etapa 1: Construcción de la aplicación
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Construcción de la imagen final con el .jar generado
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copiar el .jar generado, asegurándose de que el nombre incluye la versión (0.0.1-SNAPSHOT)
COPY --from=build /app/target/msvc-gateway-0.0.1-SNAPSHOT.jar /app/msvc-gateway.jar

# Ejecutar el .jar
ENTRYPOINT ["java", "-jar", "/app/msvc-gateway.jar"]
