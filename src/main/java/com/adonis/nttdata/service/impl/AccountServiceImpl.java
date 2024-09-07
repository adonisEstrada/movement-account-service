package com.adonis.nttdata.service.impl;

import com.adonis.nttdata.client.ClientPersonServiceClient;
import com.adonis.nttdata.entity.Account;
import com.adonis.nttdata.exeption.ValidationException;
import com.adonis.nttdata.presentation.presenter.AccountPresenter;
import com.adonis.nttdata.presentation.presenter.ClientPresenter;
import com.adonis.nttdata.presentation.presenter.MovementPresenter;
import com.adonis.nttdata.repository.AccountRepository;
import com.adonis.nttdata.service.AccountService;
import com.adonis.nttdata.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientPersonServiceClient clientPersonService;

    @Autowired
    private MovementService movementService;

    @Override
    public AccountPresenter getAccountByNumber(String accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if (!account.isPresent()) {
            throw new ValidationException("Bank account not found");
        }
        return toPresenter(account.get());
    }

    @Override
    @Transactional
    public AccountPresenter saveAccount(AccountPresenter accountPresenter) {
        String accountNumber = accountPresenter.getAccountNumber();
        if (accountNumber == null || accountNumber.isEmpty() || accountNumber.isBlank()) {
            throw new ValidationException("Bank account must have identification number");
        }
        if (accountPresenter.getClientId() == null && accountPresenter.getDni()==null) {
            throw new ValidationException("Bank account must have clientId or dni");
        }
        ClientPresenter client = clientPersonService.getClientByDni(accountPresenter.getDni());
        if (client==null) {
            throw new ValidationException("Client was not found");
        }
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalAccount.isPresent()) {
            throw new ValidationException("Bank account has already been registered");
        }
            Account account = new Account();
            account.setAccountNumber(accountPresenter.getAccountNumber());
            account.setAccountType(accountPresenter.getAccountType());
            account.setInitialBalance(accountPresenter.getInitialBalance());
            account.setStatus(accountPresenter.getStatus());
            account.setClientId(client.getClientId());
            Account accountSaved = accountRepository.save(account);
            return toPresenter(accountSaved);

    }

    @Override
    @Transactional
    public AccountPresenter updateAccount(AccountPresenter accountPresenter) {
        UUID accountId = accountPresenter.getAccountId();
        if (accountId == null) {
            throw new ValidationException("accountId must have ID");
        }
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new ValidationException("Client not found");
        }
        ClientPresenter client = clientPersonService.getClientByDni(accountPresenter.getDni());
        if (client==null) {
            throw new ValidationException("Client not found");
        }
        account.get().setAccountNumber(accountPresenter.getAccountNumber());
        account.get().setAccountType(accountPresenter.getAccountType());
        account.get().setInitialBalance(accountPresenter.getInitialBalance());
        account.get().setStatus(accountPresenter.getStatus());
        account.get().setClientId(client.getClientId());
        Account accountSaved = accountRepository.save(account.get());
        return toPresenter(accountSaved);
    }

    @Override
    @Transactional
    public void deleteAccountById(UUID accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            throw new ValidationException("Bank account not found");
        }
            if (account.get().getMovements() == null && account.get().getMovements().isEmpty()) {
                throw new ValidationException("The bank account has movement");
            }
        accountRepository.deleteById(account.get().getAccountId());
    }

    @Override
    public AccountPresenter toPresenter(Account account) {
        Set<MovementPresenter> movementPresenters = new HashSet<>();
        if (account.getMovements() != null) {
            account.getMovements().forEach(movement -> {
                movementPresenters.add(movementService.toPresenter(movement));
            });
        }
        ClientPresenter client = clientPersonService.getClientById(account.getClientId());
        return AccountPresenter.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .initialBalance(account.getInitialBalance())
                .movementPresenters(movementPresenters)
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .dni(client.getIdentification())
                .clientId(client.getClientId())
                .name(client.getName())
                .build();
    }

    @Override
    public List<AccountPresenter> getAccountsByClientId(String clientId) {
        if(clientId==null || clientId.isEmpty()){
            return null;
        }
        return accountRepository.findByClientId(UUID.fromString(clientId))
                .stream().map(this::toPresenter).collect(Collectors.toList());
    }
}
