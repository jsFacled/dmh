package com.DigitalMoneyHouse.msvc_usersAccountsManager.service;


import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IAccountClient;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IUserClient;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserRegisteredResponseDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserResponseDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.entity.UsersAccountsManager;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.repository.UsersAccountsManagerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class usersAccountsManagerService {
private final UsersAccountsManagerRepository usersAccountsManagerRepository;
    private final IUserClient userClient;
    private final IAccountClient accountClient;

    public usersAccountsManagerService(UsersAccountsManagerRepository usersAccountsManagerRepository, IUserClient userClient, IAccountClient accountClient) {
        this.usersAccountsManagerRepository = usersAccountsManagerRepository;
        this.userClient = userClient;
        this.accountClient = accountClient;
    }

    @Transactional
    public void registrarUserAccount(UserDTO userDTO) {
        /*
        * Se configura un segundo intento si fallara la primera vez la creacion de Usuario
        * */
        final int maxRetries = 2; // Número máximo de intentos para crear el usuario
        final int retryDelayMillis = 1000; // Tiempo de espera entre intentos fallidos (en milisegundos)

        Long userId = null; // Variable para almacenar el ID del usuario creado
        boolean userCreated = false; // Flag para indicar si el usuario fue creado exitosamente


        // Paso 1: Intentar crear el usuario hasta el número máximo de reintentos

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // Intentar crear el usuario utilizando el cliente de usuarios
                UserRegisteredResponseDTO userResponse = userClient.createUser(userDTO);

                // Verificar si la respuesta contiene un ID de usuario válido
                if (userResponse != null && userResponse.getUserId() != null) {
                    userId = userResponse.getUserId(); // Almacenar el ID del usuario
                    userCreated = true; // Indicar que el usuario fue creado exitosamente
                    break; // Salir del bucle si el usuario se creó correctamente
                } else {
                    // Lanzar excepción si no se pudo crear el usuario
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo crear el usuario");
                }
            } catch (Exception e) {
                if (attempt == maxRetries) {
                    // Lanzar excepción si se agotaron los intentos de reintento
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear el usuario", e);
                }
                // Esperar antes de intentar nuevamente
                try {
                    Thread.sleep(retryDelayMillis);
                } catch (InterruptedException ie) {
                    // Restaurar el estado de interrupción y lanzar una excepción si el retraso falla
                    Thread.currentThread().interrupt();
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error durante el retraso del reintento", ie);
                }
            }
        }

        // Paso 2: Si el usuario fue creado exitosamente, proceder con la creación de la cuenta.

        // Paso 2: Verificar si el usuario fue creado exitosamente
        if (userCreated) {
            try {
                // Verificar que el usuario realmente existe
                UserResponseDTO existingUser = userClient.getUserById(userId);
                if (existingUser != null) {
                    // Intentar crear la cuenta para el usuario utilizando el cliente de cuentas
                    accountClient.createAccount(userId);
                    // Guardar la información en la base de datos
                    UsersAccountsManager usersAccounts = new UsersAccountsManager();
                    usersAccounts.setUserId(userId);
                    // Configura otros campos si es necesario
                    usersAccountsManagerRepository.save(usersAccounts);
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Usuario no encontrado después de la creación");
                }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear la cuenta", e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear el usuario después de los reintentos");
        }
    }
}
