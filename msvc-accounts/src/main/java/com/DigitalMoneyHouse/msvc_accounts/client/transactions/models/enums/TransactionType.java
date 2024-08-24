package com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums;


import lombok.Getter;

@Getter
public enum TransactionType {
    RECHARGE_FROM_CARD("Recarga desde tarjeta"),
    TRANSFER_BETWEEN_ACCOUNTS("Transferencia entre cuentas");


    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

}
