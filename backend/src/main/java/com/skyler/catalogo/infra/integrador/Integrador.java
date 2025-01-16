package com.skyler.catalogo.infra.integrador;

import com.skyler.catalogo.infra.integrador.DTOs.FranquiaIntegrador;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Integrador extends IntegradorContext implements ApplicationListener<ApplicationReadyEvent> {

    private final IntegradorFranquiasELojas integradorFranquiasELojas;

    public Integrador(IntegradorFranquiasELojas integradorFranquiasELojas) {
        this.integradorFranquiasELojas = integradorFranquiasELojas;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.integradorFranquiasELojas.updateLojasAndFranquias();
    }

}
