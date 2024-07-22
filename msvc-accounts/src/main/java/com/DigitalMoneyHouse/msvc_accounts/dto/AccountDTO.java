package com.DigitalMoneyHouse.msvc_accounts.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

//@AllArgsConstructor
//@NoArgsConstructor
@Data
public class AccountDTO {
        private Long id;
        private Long userId;
        private BigDecimal balance;
        private String cvu;
        private String alias;

}
