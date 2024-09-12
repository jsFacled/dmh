package com.DigitalMoneyHouse.msvc_transactions.models.entity;

import com.DigitalMoneyHouse.msvc_transactions.models.enums.ProductOriginType;
import com.DigitalMoneyHouse.msvc_transactions.models.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @Column(name = "origin_account_id")
    private Long originAccountId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_origin_type")
    private ProductOriginType productOriginType;

    @Column(name = "product_origin_id")
    private Long productOriginId;

    @Column(name = "destination_account_id")
    private Long destinationAccountId;


    private BigDecimal amount;
    private String description;

    // Getters y Setters
}
