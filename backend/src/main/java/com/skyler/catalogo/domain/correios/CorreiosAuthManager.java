package com.skyler.catalogo.domain.correios;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

public abstract class CorreiosAuthManager {

    private String token;

    @Scheduled(cron = "0 0 * * * *")
    private void renovarToken(){
        RestTemplate restTemplate = new RestTemplate();

    }
}
