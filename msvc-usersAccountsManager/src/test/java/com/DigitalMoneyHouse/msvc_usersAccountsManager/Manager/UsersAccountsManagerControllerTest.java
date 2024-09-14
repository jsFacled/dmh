package com.DigitalMoneyHouse.msvc_usersAccountsManager.Manager;

import com.DigitalMoneyHouse.msvc_usersAccountsManager.controller.UsersAccountsManagerController;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.service.UsersAccountsManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static java.nio.file.Paths.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersAccountsManagerController.class)
public class UsersAccountsManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersAccountsManagerService usersAccountsManagerService;

    @Test
    void testHello() throws Exception {
        mockMvc.perform((RequestBuilder) get("/manager/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola Mundo!! Soy ms-Manager!!!"));
    }

    @Test
    void testRegisterUserAccount_Success() throws Exception {
        // Crear el DTO de prueba
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Juan");
        userDTO.setLastName("Perez");
        userDTO.setDni("12345678");
        userDTO.setEmail("juan.perez@example.com");
        userDTO.setPhone("123456789");
        userDTO.setPassword("password");

        // Simular el servicio de registro
        when(usersAccountsManagerService.registrarUserAccount(any(UserDTO.class))).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        // Ejecutar la prueba del controlador
        mockMvc.perform(post("/manager/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testValidateUserForAccount_Success() throws Exception {
        // Simular respuesta del servicio
        when(usersAccountsManagerService.isAuthorizedForAccount(1L, 1L)).thenReturn(true);

        // Ejecutar la prueba del controlador
        mockMvc.perform((RequestBuilder) get("/manager/validate/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testValidateUserForAccount_Failure() throws Exception {
        // Simular respuesta del servicio
        when(usersAccountsManagerService.isAuthorizedForAccount(1L, 1L)).thenReturn(false);

        // Ejecutar la prueba del controlador
        mockMvc.perform((RequestBuilder) get("/manager/validate/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
