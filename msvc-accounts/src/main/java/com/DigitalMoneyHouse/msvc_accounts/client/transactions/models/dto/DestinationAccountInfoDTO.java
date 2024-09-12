package com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DestinationAccountInfoDTO {
    private Long id;
    private String cvu;
    private String alias;



}