package com.skyler.catalogo.infra.integrador;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.infra.integrador.DTOs.FranquiaIntegrador;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IntegradorFranquiasELojas extends IntegradorContext{

    private final IntegradorBridge integradorBridge;
    private final FranquiaRepository franquiaRepository;
    private final LojaRepository lojaRepository;

    public IntegradorFranquiasELojas(IntegradorBridge integradorBridge, FranquiaRepository franquiaRepository, LojaRepository lojaRepository) {
        this.integradorBridge = integradorBridge;
        this.franquiaRepository = franquiaRepository;
        this.lojaRepository = lojaRepository;
    }

    @Transactional
    public void updateLojasAndFranquias(){
        List<FranquiaIntegrador> franquias = integradorBridge.getFranquiasAndLojas();
        List<Franquia> franquiasEnt = new ArrayList<>();
        List<String> runnedNomes = new ArrayList<>();
        for(FranquiaIntegrador franquia:franquias){
            Franquia franquiaEnt = new Franquia();
            Optional<Franquia> franquiaOPT = this.franquiaRepository.findByIntegradorId(franquia.getIntegradorId());
            if(franquiaOPT.isPresent()){
                franquiaEnt = franquiaOPT.get();
            }
            franquiaEnt.setIntegradorId(franquia.getIntegradorId());
            franquiaEnt.setNome(franquia.getNome());
            franquiaEnt.setIsMatriz(franquia.getIsMatriz());
            franquiaEnt.setCnpj(franquia.getCnpj());
            for(FranquiaIntegrador.Loja loja:franquia.getLojas()){
                if(!runnedNomes.contains(loja.getNome())){
                    Loja lojaEnt = new Loja();
                    Optional<Loja> lojaOPT = this.lojaRepository.findByIntegradorId(loja.getIntegradorId());
                    if(lojaOPT.isPresent()){
                        lojaEnt = lojaOPT.get();
                    }
                    lojaEnt.setSlug(loja.getNome().toLowerCase().trim().replace("-","").replaceAll("\\s+", "-"));
                    lojaEnt.setNome(loja.getNome());
                    lojaEnt.setErpId(loja.getErpId());
                    lojaEnt.setIntegradorId(loja.getIntegradorId());
                    franquiaEnt.addLojaIfNotExists(lojaEnt);
                    runnedNomes.add(loja.getNome());
                }
            }
            franquiasEnt.add(franquiaEnt);
        }
        franquiaRepository.saveAll(franquiasEnt);
    }
}
