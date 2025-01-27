package com.skyler.catalogo.domain.descontos.carrinho.services;

import com.skyler.catalogo.domain.descontos.carrinho.repositories.DescontoRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DescontosCron implements ApplicationListener<ApplicationReadyEvent> {

    private final DescontoRepository descontoRepository;

    public DescontosCron(DescontoRepository descontoRepository) {
        this.descontoRepository = descontoRepository;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    public void expirados(){
        try{
            this.descontoRepository.updateExpirados(LocalDate.now());
        }catch (Exception e){
            System.out.println("Erro ao atualizar expirados:");
            System.out.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.expirados();
    }
}
