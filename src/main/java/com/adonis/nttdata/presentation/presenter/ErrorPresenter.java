package com.adonis.nttdata.presentation.presenter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorPresenter {
    private String message;
    private String details;
}
