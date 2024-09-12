package com.DigitalMoneyHouse.msvc_usersAccountsManager.service;


import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.autModels.LoginRequestDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IAccountClient;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IUserClient;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.AccountRegisteredResponseDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserRegisteredResponseDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UsersAccountsManagerDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.entity.UsersAccountsManager;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.exceptions.UserAlreadyExistsException;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.repository.UsersAccountsManagerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
@Service
public class UsersAccountsManagerService implements  IUsersAccountsManagerService{

    private final UsersAccountsManagerRepository usersAccountsManagerRepository;
    private final IUserClient userClient;
    private final IAccountClient accountClient;

    public UsersAccountsManagerService(UsersAccountsManagerRepository usersAccountsManagerRepository, IUserClient userClient, IAccountClient accountClient) {
        this.usersAccountsManagerRepository = usersAccountsManagerRepository;
        this.userClient = userClient;
        this.accountClient = accountClient;
    }

    @Transactional
    public ResponseEntity<?> registrarUserAccount(UserDTO userDTO) {
/**
 * Este método maneja la creación de un usuario y su cuenta asociada,
 * incluyendo manejo de reintentos y manejo de excepciones para diversos escenarios de fallo.
 */

        // Número máximo de intentos para crear el usuario
        final int maxRetries = 2;
        // Tiempo de espera entre intentos fallidos (en milisegundos)
        final int retryDelayMillis = 1000;

        Long userId = null; // Variable para almacenar el ID del usuario creado
        boolean userCreated = false; // Flag para indicar si el usuario fue creado exitosamente

        // Paso 1: Intentar crear el usuario hasta el número máximo de reintentos
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // Intentar crear el usuario utilizando el cliente Feign de usuarios
                UserRegisteredResponseDTO userRegisteredResponseDTO = userClient.createUser(userDTO);

                // Verificar si la respuesta contiene un ID de usuario válido
                if (userRegisteredResponseDTO != null && userRegisteredResponseDTO.getUserId() != null) {
                    userId = userRegisteredResponseDTO.getUserId(); // Almacenar el ID del usuario

                    System.out.println("Se creó el usuario con user_id " + userId);
                    userCreated = true; // Indicar que el usuario fue creado exitosamente
                    break; // Salir del bucle si el usuario se creó correctamente
                } else {
                    // Lanzar excepción si no se pudo crear el usuario
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo crear el usuario");
                }
            } catch (UserAlreadyExistsException e) {
                if (attempt == maxRetries) {
                    // Lanzar excepción si se agotaron los intentos de reintento
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "UserAlreadyExistsException: " + e.getMessage(), e);
                }
                // Esperar antes de intentar nuevamente
                try {
                    Thread.sleep(retryDelayMillis);
                } catch (InterruptedException ie) {
                    // Restaurar el estado de interrupción y lanzar una excepción si el retraso falla
                    Thread.currentThread().interrupt();
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error durante el retraso del reintento", ie);
                }

            } catch (Exception e) {
                if (attempt == maxRetries) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear el usuario", e);
                }
                try {
                    Thread.sleep(retryDelayMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error durante el retraso del reintento", ie);
                }
            }

        }

        // Paso 2: Si el usuario fue creado exitosamente, proceder con la creación de la cuenta
        if (userCreated) {
            try {
                // Intentar crear una cuenta utilizando el cliente Feign de cuentas
                ResponseEntity<AccountRegisteredResponseDTO> responseEntityAccountRegistered = accountClient.createAccount(userId);

                if (responseEntityAccountRegistered.getStatusCode() == HttpStatus.CREATED) {
                    // Extraer el DTO de accountRegistered y asignar los valores
                    AccountRegisteredResponseDTO createdAccount = responseEntityAccountRegistered.getBody();

                    System.out.println("Se creó la cuenta con account_id " + createdAccount.getId());

                    // Crear la entidad UsersAccountsManager
                    UsersAccountsManager usersAccountsManager = new UsersAccountsManager();
                    usersAccountsManager.setUserId(userId);
                    usersAccountsManager.setAccountId(createdAccount.getId());
                    usersAccountsManager.setCreatedAt(LocalDateTime.now());

                    // Guardar la información del manager en la base de datos
                    UsersAccountsManager uaManagerSaved = usersAccountsManagerRepository.save(usersAccountsManager);

                    // Crear el DTO a partir de la base de datos
                    UsersAccountsManagerDTO usersAccountsManagerDTO = new UsersAccountsManagerDTO(
                            uaManagerSaved.getId(),
                            uaManagerSaved.getUserId(),
                            uaManagerSaved.getAccountId()
                    );

                    // Devolver la respuesta con el estado CREATED
                    return ResponseEntity.status(HttpStatus.CREATED).body(usersAccountsManagerDTO);
                } else {
                    // Lanzar excepción si no se pudo crear la cuenta
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Usuario no encontrado después de la creación");
                }
            } catch (Exception e) {
                // Lanzar excepción si ocurre un error durante la creación de la cuenta
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear la cuenta", e);
            }
        } else {
            // Lanzar excepción si el usuario no se creó después de los reintentos
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear el usuario después de los reintentos");
        }
    }

    @Override
    public boolean isAuthorizedForAccount(Long accountId, Long userId) {
        boolean isUserWhitAccount = usersAccountsManagerRepository.isAuthorizedForAccount(accountId, userId);
        System.out.println("La cuenta < "+accountId+" > está asociada con el usuario < "+userId+" >" );
        return isUserWhitAccount;
    }

}

