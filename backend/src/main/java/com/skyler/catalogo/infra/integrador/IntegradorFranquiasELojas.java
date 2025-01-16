package com.skyler.catalogo.infra.integrador;

import com.skyler.catalogo.infra.integrador.DTOs.FranquiaIntegrador;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntegradorFranquiasELojas extends IntegradorContext{

    private final IntegradorBridge integradorBridge;

    public IntegradorFranquiasELojas(IntegradorBridge integradorBridge) {
        this.integradorBridge = integradorBridge;
    }


    @Transactional
    public void updateLojasAndFranquias(){
        List<FranquiaIntegrador> franquias = integradorBridge.getFranquiasAndLojas();
        System.out.println("TESTE");
    }
}
