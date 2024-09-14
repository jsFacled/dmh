package com.DigitalMoneyHouse.msvc_usersAccountsManager.Manager;


import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IAccountClient;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IUserClient;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.entity.UsersAccountsManager;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.repository.UsersAccountsManagerRepository;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.service.UsersAccountsManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UsersAccountsManagerServiceIntegrationTest {

    @Autowired
    private UsersAccountsManagerService usersAccountsManagerService;

    @Autowired
    private UsersAccountsManagerRepository usersAccountsManagerRepository;

    @Autowired
    private IUserClient userClient;

    @Autowired
    private IAccountClient accountClient;

    @Test
    @Transactional
    void testRegisterUserAccount_Integration() throws Exception {
        // Crear un UserDTO de prueba
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Pedro");
        userDTO.setLastName("Ramirez");
        userDTO.setDni("98765432");
        userDTO.setEmail("pedro.ramirez@example.com");
        userDTO.setPhone("987654321");
        userDTO.setPassword("password");

        // Ejecutar el servicio de integración
        ResponseEntity<?> response = usersAccountsManagerService.registrarUserAccount(userDTO);

        // Verificar que el usuario y la cuenta fueron creados correctamente
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar que se creó una entrada en la tabla users_accounts_manager
        List<UsersAccountsManager> allEntries = usersAccountsManagerRepository.findAll();
        assertFalse(allEntries.isEmpty());
    }

    @Test
    void testValidateUserForAccount_Integration() {
        // Preconfigurar datos en la base de datos
        UsersAccountsManager usersAccountsManager = new UsersAccountsManager();
        usersAccountsManager.setUserId(1L);
        usersAccountsManager.setAccountId(1L);
        usersAccountsManager.setCreatedAt(LocalDateTime.now());
        usersAccountsManagerRepository.save(usersAccountsManager);

        // Ejecutar el servicio de validación
        boolean hasAccess = usersAccountsManagerService.isAuthorizedForAccount(1L, 1L);

        // Verificar que el usuario tenga acceso a la cuenta
        assertTrue(hasAccess);
    }
}
