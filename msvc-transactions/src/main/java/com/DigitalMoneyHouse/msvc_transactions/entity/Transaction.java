package com.DigitalMoneyHouse.msvc_transactions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;



@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;
    private Double amount;
    private String type; // Ejemplo: "DEBIT" o "CREDIT"
    private String description;
    private LocalDateTime date;

    // Getters y Setters
}
