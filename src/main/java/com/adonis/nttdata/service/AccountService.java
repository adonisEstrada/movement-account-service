package com.adonis.nttdata.service;

import com.adonis.nttdata.entity.Account;
import com.adonis.nttdata.presentation.presenter.AccountPresenter;

import java.util.List;
import java.util.UUID;

public interface AccountService {

   AccountPresenter getAccountByNumber(String accountNumber);

   AccountPresenter saveAccount(AccountPresenter accountPresenter);

   AccountPresenter updateAccount(AccountPresenter accountPresenter);

   void deleteAccountById(UUID accountId);

   AccountPresenter toPresenter(Account account);

   List<AccountPresenter> getAccountsByClientId(String clientId);
}
