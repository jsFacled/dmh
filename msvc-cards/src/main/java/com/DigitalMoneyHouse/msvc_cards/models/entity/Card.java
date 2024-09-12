package com.DigitalMoneyHouse.msvc_cards.models.entity;
import com.DigitalMoneyHouse.msvc_cards.models.enums.CardBrand;
import com.DigitalMoneyHouse.msvc_cards.models.enums.CardType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorColumn(name = "dtype")
public abstract class Card {
    /**
     * Credito o Débito se crearán a partir de Card
     * Se podrá:
     *  - Cargar saldo
     *  - Pagar servicios
     *  - dtype lo genera Hibernate automaticamente para indicar las subclases.
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


    @Column(name = "account_id", nullable = false)
    private Long accountId;


    @Column(name = "user_id", nullable = false) //pertenece a un usuario
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "brand", nullable = false)
    private CardBrand brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;


}
