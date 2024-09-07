package com.adonis.nttdata.entity;


import com.adonis.nttdata.enums.AccountStatus;
import com.adonis.nttdata.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
@ToString(of = "accountId")
@EqualsAndHashCode(of = "accountId")
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue
    private UUID accountId;

    private UUID clientId;

    @NotNull
    @Column(unique = true)
    private String accountNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private BigDecimal initialBalance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToMany(mappedBy = "account")
    private Set<Movement> movements;
}
