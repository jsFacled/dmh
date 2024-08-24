package com.DigitalMoneyHouse.msvc_accounts.client.transactions.models;

import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.ProductOriginType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionDTO {

    private Long id;
    private LocalDateTime date;
    private Long originAccountId;
    private TransactionType type;
    private ProductOriginType productOriginType;
    private Long productOriginId;
    private Long destinationAccountId;
    private BigDecimal amount;
    private String description;

}
