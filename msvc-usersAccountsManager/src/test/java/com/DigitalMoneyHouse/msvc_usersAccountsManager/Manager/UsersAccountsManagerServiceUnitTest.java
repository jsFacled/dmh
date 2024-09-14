package com.DigitalMoneyHouse.msvc_usersAccountsManager.Manager;



import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IAccountClient;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IUserClient;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.AccountRegisteredResponseDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserRegisteredResponseDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UsersAccountsManagerDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.exceptions.UserAlreadyExistsException;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.repository.UsersAccountsManagerRepository;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.service.UsersAccountsManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UsersAccountsManagerServiceUnitTest {

    @Mock
    private UsersAccountsManagerRepository usersAccountsManagerRepository;

    @Mock
    private IUserClient userClient;

    @Mock
    private IAccountClient accountClient;

    @InjectMocks
    private UsersAccountsManagerService usersAccountsManagerService;

    private UserDTO userDTO;
    private AccountRegisteredResponseDTO accountRegisteredResponseDTO;
    private UserRegisteredResponseDTO userRegisteredResponseDTO;

    @BeforeEach
    void setUp() {
        // Datos de prueba para un usuario y cuenta
        userDTO = new UserDTO();
        userDTO.setFirstName("Juan");
        userDTO.setLastName("Perez");
        userDTO.setDni("12345678");
        userDTO.setEmail("juan.perez@example.com");
        userDTO.setPhone("123456789");
        userDTO.setPassword("password");

        userRegisteredResponseDTO = new UserRegisteredResponseDTO(1L, "Usuario registrado exitosamente");
        accountRegisteredResponseDTO = new AccountRegisteredResponseDTO(1L, 1L);
    }

    @Test
    void testRegisterUserAccount_Success() throws Exception {
        // Simular respuestas de los clientes Feign
        when(userClient.createUser(any(UserDTO.class))).thenReturn(userRegisteredResponseDTO);
        when(accountClient.createAccount(anyLong())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(accountRegisteredResponseDTO));

        // Ejecutar el servicio
        ResponseEntity<?> response = usersAccountsManagerService.registrarUserAccount(userDTO);

        // Verificar que el servicio haya devuelto un resultado CREATED
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof UsersAccountsManagerDTO);
    }

    @Test
    void testRegisterUserAccount_UserAlreadyExists() {
        // Simular excepción al intentar registrar un usuario ya existente
        when(userClient.createUser(any(UserDTO.class))).thenThrow(new UserAlreadyExistsException("El usuario ya existe"));

        // Ejecutar el servicio
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usersAccountsManagerService.registrarUserAccount(userDTO);
        });

        // Verificar que la excepción sea de tipo CONFLICT
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void testIsAuthorizedForAccount_Success() {
        // Simular que el usuario tiene acceso a la cuenta
        when(usersAccountsManagerRepository.isAuthorizedForAccount(anyLong(), anyLong())).thenReturn(true);

        // Ejecutar el método
        boolean hasAccess = usersAccountsManagerService.isAuthorizedForAccount(1L, 1L);

        // Verificar el resultado
        assertTrue(hasAccess);
    }

    @Test
    void testIsAuthorizedForAccount_Failure() {
        // Simular que el usuario no tiene acceso a la cuenta
        when(usersAccountsManagerRepository.isAuthorizedForAccount(anyLong(), anyLong())).thenReturn(false);

        // Ejecutar el método
        boolean hasAccess = usersAccountsManagerService.isAuthorizedForAccount(1L, 2L);

        // Verificar el resultado
        assertFalse(hasAccess);
    }
}
