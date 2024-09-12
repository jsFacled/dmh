package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthControllerTest {

    @Test
    public void testLoginSuccess() {
        // Configura la URL base para las pruebas
        RestAssured.baseURI = "http://localhost:8081/auth";

        // Crear un objeto LoginRequestDTO con los datos de login
        String loginRequestJson = "{ \"email\": \"bbbb@gmail.com\", \"password\": \"bbbbb\" }";

        // Realiza la solicitud POST para el login
        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginRequestJson)
                .when()
                .post("/login")
                .then()
                .statusCode(200) // Verifica que el código de respuesta sea 200 (OK)
                .extract().response();

        // Extraer el token de la respuesta
        String token = response.getBody().asString();
        System.out.println("Token recibido: " + token);

        // Verificar que el token no sea nulo o vacío
        assertEquals(false, token.isEmpty(), "El token JWT no debería estar vacío.");
    }

    @Test
    public void testLoginFailure() {
        // Configura la URL base para las pruebas
        RestAssured.baseURI = "http://localhost:8081/auth";

        // Crear un objeto LoginRequestDTO con credenciales incorrectas
        String loginRequestJson = "{ \"email\": \"invalid@example.com\", \"password\": \"wrongpassword\" }";

        // Realiza la solicitud POST para el login
        given()
                .contentType(ContentType.JSON)
                .body(loginRequestJson)
                .when()
                .post("/login")
                .then()
                .statusCode(401) // Verifica que el código de respuesta sea 401 (Unauthorized)
                .body(equalTo(" * * L41 * * Invalid credentials desde linea 28 en metodo login del AuthController. Pero el context holder es: EmptySecurityContextImpl []")); // Opcional: Verifica el mensaje de error
    }



}

