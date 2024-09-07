package com.adonis.nttdata.entity;


import com.adonis.nttdata.enums.MovementType;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movements")
@EqualsAndHashCode(of = "movementId")
@Builder
public class Movement {

    @Id
    @GeneratedValue
    private UUID movementId;

    private Date transactionDate;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    private BigDecimal amount;

    private BigDecimal currentBalance;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @NotNull
    private Account account;

}
