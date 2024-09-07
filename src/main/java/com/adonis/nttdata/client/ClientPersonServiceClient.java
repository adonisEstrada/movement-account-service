package com.adonis.nttdata.client;

import com.adonis.nttdata.presentation.presenter.ClientPresenter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "client-service", url = "${feign.client.client-person}")
public interface ClientPersonServiceClient {

    @GetMapping("/getClientById")
    ClientPresenter getClientById(@RequestParam("clientId") UUID clientId);

    @GetMapping("/getClientByDni")
    ClientPresenter getClientByDni(@RequestParam("dni")String dni);
}
