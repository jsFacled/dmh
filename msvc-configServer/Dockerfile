# Etapa de construcción
FROM maven:3.9.0-openjdk-17 AS build
WORKDIR /app

# Copia el código fuente y pom.xml
COPY . .

# Construye el proyecto
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia el JAR construido desde la etapa de construcción
COPY --from=build /app/target/msvc-configServer-0.0.1-SNAPSHOT.jar /app/msvc-configserver.jar

# Expone el puerto en el que la aplicación escuchará
EXPOSE 8888

# Comando para ejecutar el JAR
ENTRYPOINT ["java", "-jar", "msvc-configserver.jar"]
