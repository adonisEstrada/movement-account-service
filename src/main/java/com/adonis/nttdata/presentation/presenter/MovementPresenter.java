package com.adonis.nttdata.presentation.presenter;

import com.adonis.nttdata.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementPresenter {
    private String movementId;
    private MovementType movementType;
    private Date transactionDate;
    private BigDecimal amount;
    private BigDecimal currentBalance;
    private String accountId;
}
