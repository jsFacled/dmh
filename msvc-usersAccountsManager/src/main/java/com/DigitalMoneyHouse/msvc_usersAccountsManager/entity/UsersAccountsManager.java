package com.DigitalMoneyHouse.msvc_usersAccountsManager.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class UsersAccountsManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "account_id")
    private Long accountId;

    @ElementCollection
    @CollectionTable(name = "users_accounts_cards", joinColumns = @JoinColumn(name = "manager_id"))
    @Column(name = "card_id")
    private List<Long> cardsId = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
