package com.adonis.challenge.presentation.presenter;

import com.adonis.challenge.enums.AccountStatus;
import com.adonis.challenge.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountPresenter {

    private UUID accountId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal initialBalance;
    private AccountStatus status;
    private UUID clientId;
    private String dni;
    private String name;
    private Set<MovementPresenter> movementPresenters;
}
