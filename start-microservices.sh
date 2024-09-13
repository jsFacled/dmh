#!/bin/bash

# Inicia el microservicio msvc-configServer
mintty bash -c "cd msvc-configServer && ./mvnw spring-boot:run; exec bash"

# Espera 10 segundos para asegurarse de que msvc-configServer esté completamente en funcionamiento
sleep 10

# Inicia el microservicio msvc-eureka
mintty bash -c "cd msvc-eureka && ./mvnw spring-boot:run; exec bash"

# Espera 10 segundos para asegurarse de que msvc-eureka esté completamente en funcionamiento
sleep 10

# Inicia los otros microservicios
mintty bash -c "cd msvc-accpimts && ./mvnw spring-boot:run; exec bash"
mintty bash -c "cd msvc-users && ./mvnw spring-boot:run; exec bash"
mintty bash -c "cd msvc-cards && ./mvnw spring-boot:run; exec bash"
mintty bash -c "cd msvc-transactions && ./mvnw spring-boot:run; exec bash"
mintty bash -c "cd msvc-usersAccountsManager && ./mvnw spring-boot:run; exec bash"
mintty bash -c "cd msvc-gateway && ./mvnw spring-boot:run; exec bash"

echo "Todos los microservicios están en ejecución."


#Primero dar permiso desde consola gitbash:
## chmod +x start-microservices.sh

#Luego correr
## ./start-microservices.sh
