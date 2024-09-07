package com.adonis.nttdata.presentation.presenter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ClientPresenter extends PersonPresenter{
    private UUID clientId;
    private String password;
    private Boolean status;
    private Set<AccountPresenter> accountPresenters;
}
