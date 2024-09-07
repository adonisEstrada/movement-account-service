package com.adonis.nttdata.presentation.presenter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PersonPresenter {

    private UUID personId;
    private String name;
    private String gender;
    private Integer age;
    private String identification;
    private String address;
    private String phoneNumber;

}