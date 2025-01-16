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
    private final IntegradorProdutos integradorProdutos;

    public Integrador(IntegradorFranquiasELojas integradorFranquiasELojas, IntegradorProdutos integradorProdutos) {
        this.integradorFranquiasELojas = integradorFranquiasELojas;
        this.integradorProdutos = integradorProdutos;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(updateLojasAndFranquiasOnConstruct){
            this.integradorFranquiasELojas.updateLojasAndFranquias();//criar cron
        }
        if(updateProdutosOnConstruct){
            this.integradorProdutos.updateProdutos();//criar cron
        }
    }

}
