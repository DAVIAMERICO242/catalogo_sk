package com.skyler.catalogo.infra.integrador;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.infra.integrador.DTOs.FranquiaIntegrador;
import com.skyler.catalogo.infra.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IntegradorFranquiasELojas extends IntegradorContext{


    private final UpdateLojaAndFranquiaMethod updateLojaAndFranquiaMethod;
    private final IntegradorBridge integradorBridge;

    public IntegradorFranquiasELojas(UpdateLojaAndFranquiaMethod updateLojaAndFranquiaMethod, IntegradorBridge integradorBridge) {
        this.updateLojaAndFranquiaMethod = updateLojaAndFranquiaMethod;
        this.integradorBridge = integradorBridge;
    }


    public void updateLojasAndFranquias(){
        List<FranquiaIntegrador> franquias = integradorBridge.getFranquiasAndLojas();
        List<String> runnedNomes = new ArrayList<>();
        for(FranquiaIntegrador franquia:franquias){
            this.updateLojaAndFranquiaMethod.updateLojaAndFranquia(franquia,runnedNomes);
        }
    }
}
