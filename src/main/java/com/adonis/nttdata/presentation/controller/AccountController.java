package com.adonis.nttdata.presentation.controller;

import com.adonis.nttdata.presentation.presenter.AccountPresenter;
import com.adonis.nttdata.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/getAccountByNumber")
    public AccountPresenter getAccountByNumber(@RequestParam("accountNumber") String accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }

    @PostMapping("/createAccount")
    AccountPresenter saveAccount(@RequestBody AccountPresenter accountPresenter) {
        return accountService.saveAccount(accountPresenter);
    }

    @PutMapping("/putAccount")
    AccountPresenter updateAccount(@RequestBody AccountPresenter accountPresenter) {
        return accountService.updateAccount(accountPresenter);
    }

    @DeleteMapping("/deleteAccountById")
    public void deleteAccountById(@RequestParam("accountId") UUID accountId) {
        accountService.deleteAccountById(accountId);
    }

    @PostMapping("/getAccountByClientId")
    public List<AccountPresenter> getAccountsByClientId(String clientId) {
        return accountService.getAccountsByClientId(clientId);
    }

}
