package com.DigitalMoneyHouse.msvc_cards.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public abstract class Card {
    /**
     * Credito o Débito se crearán a partir de Card
     * Se podrá:
     *  - Cargar saldo
     *  - Pagar servicios
     *
     */


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "expiration")
    private String expiration;
    @Column(name = "number", unique = true)
    private String number;

    @Column(name = "holder_name")
    private String holderName;

    @Column(name = "cvc")
    private String cvc; // Código de seguridad de la tarjeta (CVC/CVV)


    @Column(name = "account_id")//puede ser nulo porque se puede asociar y desasociar a la cuenta
    private Long accountId;


    @Column(name = "user_id", nullable = false) //pertenece a un usuario
    private Long userId;


}
