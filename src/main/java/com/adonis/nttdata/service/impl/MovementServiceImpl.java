package com.adonis.nttdata.service.impl;

import com.adonis.nttdata.client.ClientPersonServiceClient;
import com.adonis.nttdata.entity.Account;
import com.adonis.nttdata.entity.Movement;
import com.adonis.nttdata.enums.MovementType;
import com.adonis.nttdata.exeption.ValidationException;
import com.adonis.nttdata.presentation.presenter.ClientPresenter;
import com.adonis.nttdata.presentation.presenter.MovementPresenter;
import com.adonis.nttdata.presentation.presenter.MovementsReportPresenter;
import com.adonis.nttdata.repository.AccountRepository;
import com.adonis.nttdata.repository.MovementRepository;
import com.adonis.nttdata.service.MovementService;
import com.adonis.nttdata.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MovementServiceImpl implements MovementService {

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientPersonServiceClient clientRepository;

    @Override
    public Movement getMovementById(UUID movementId) {
        Optional<Movement> movement = movementRepository.findById(movementId);
        if (movement.isPresent()) {
            throw new ValidationException("Movement not found");
        }
        return movement.get();
    }

    @Override
    public MovementPresenter saveUpdateMovement(MovementPresenter movementPresenter)throws RuntimeException {
        Movement movement;
        if (movementPresenter.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("The amount of movement must be greater than 0");
        }

        if (movementPresenter.getAccountId() == null) {
            throw new ValidationException("Movement must have accountId");
        }

        Optional<Account> account = accountRepository.findByAccountNumber(movementPresenter.getAccountId());
        if (account.isEmpty()) {
            throw new ValidationException("Bank account not found");
        }
        if (movementPresenter.getMovementId() == null) {
            movement = new Movement();
        } else {
            movement = movementRepository.findById(UUID.fromString(movementPresenter.getMovementId()))
                    .orElse(new Movement());
        }
        movement.setMovementType(movementPresenter.getMovementType());
        movement.setTransactionDate(new Date());
        movement.setAmount(movementPresenter.getAmount());
        movement.setAccount(account.get());
        BigDecimal balance = calculateBalance(movement);
        movement.setCurrentBalance(balance);
        Movement movementSaved = movementRepository.save(movement);
        return toPresenter(movementSaved);

    }

    @Override
    public void deleteMovementById(UUID movementId) {
        Optional<Movement> movement = movementRepository.findById(movementId);
        if (!movement.isPresent()) {
            throw new ValidationException("Movement not found");
        }
        movementRepository.deleteById(movement.get().getMovementId());
    }

    @Override
    public List<MovementsReportPresenter> getMovementByClientAndDates(String dni, Date initDate, Date endDate) {
        List<MovementsReportPresenter> movementsReportPresenters = new ArrayList<>();
        if (initDate != null) {
            initDate = DateUtils.instance().asDate(
                    DateUtils.instance().asLocalDateTime(initDate)
                            .withHour(0).withSecond(0).withMinute(0).withNano(0)
            );
        }
        if (endDate != null) {
            endDate = DateUtils.instance().asDate(
                    DateUtils.instance().asLocalDateTime(endDate).withHour(23).withMinute(59).withSecond(59).withNano(0)
            );
        }
        if ((initDate != null && endDate != null) && initDate.compareTo(endDate) > 0) {
            throw new ValidationException("El rango de fechas es inválido");
        }
        ClientPresenter client = clientRepository.getClientByDni(dni);
        if (client == null) {
            throw new ValidationException("Client not found");
        }
        List<Object[]> movements = movementRepository.getMovementByClientAndDates(client.getPersonId(), initDate, endDate);
        movements.forEach(object ->
                movementsReportPresenters.add(MovementsReportPresenter.builder()
                        .date(object[0].toString())
                        .client((String) object[1])
                        .accountNumber((String) object[2])
                        .accountType((String) object[3])
                        .initialBalance((BigDecimal) object[4])
                        .status((String) object[5])
                        .movementAmount((BigDecimal) object[6])
                        .availableBalance((BigDecimal) object[7])
                        .build())
        );
        return movementsReportPresenters;
    }

    @Override
    public MovementPresenter toPresenter(Movement movement) {
        return MovementPresenter.builder()
                .movementId(movement.getMovementId().toString())
                .accountId(movement.getAccount().getAccountNumber())
                .transactionDate(movement.getTransactionDate())
                .movementType(movement.getMovementType())
                .amount(movement.getAmount())
                .currentBalance(movement.getCurrentBalance())
                .build();
    }

    private BigDecimal calculateBalance(Movement movement) throws RuntimeException {
        AtomicReference<BigDecimal> balance = new AtomicReference<>(BigDecimal.ZERO);
        Optional<Account> account = accountRepository.findById(movement.getAccount().getAccountId());
        if (account.get().getMovements().isEmpty()) {
            balance.set(account.get().getInitialBalance());
            if (movement.getMovementType().equals(MovementType.INCOMING)) {
                balance.set(balance.get().add(movement.getAmount()));
            } else if (movement.getMovementType().equals(MovementType.OUTGOING)) {
                balance.set(balance.get().subtract(movement.getAmount()));
            }
        }else {

            BigDecimal in = account.get().getMovements().stream().filter(m -> m.getMovementType().equals(MovementType.INCOMING)).map(m-> m.getAmount()).reduce(BigDecimal.ZERO,BigDecimal::add);
            BigDecimal out = account.get().getMovements().stream().filter(m -> m.getMovementType().equals(MovementType.OUTGOING)).map(m-> m.getAmount()).reduce(BigDecimal.ZERO,BigDecimal::add);
            balance.set(account.get().getInitialBalance().add(in).subtract(out));
            if (movement.getMovementType().equals(MovementType.INCOMING)) {
                balance.set(balance.get().add(movement.getAmount()));
            } else if (movement.getMovementType().equals(MovementType.OUTGOING)) {
                balance.set(balance.get().subtract(movement.getAmount()));
            }
        }
        if (balance.get().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Balance Not Available for this transaction");
        }
        return balance.get();
    }

}
