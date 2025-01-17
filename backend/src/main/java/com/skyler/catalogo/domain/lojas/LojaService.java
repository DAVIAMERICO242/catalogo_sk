package com.skyler.catalogo.domain.lojas;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LojaService {

    private final LojaRepository lojaRepository;

    public LojaService(LojaRepository lojaRepository) {
        this.lojaRepository = lojaRepository;
    }

    public List<LojaDTO> getLojas(){
        List<LojaDTO> output = new ArrayList<>();
        List<Loja> lojasEnt = this.lojaRepository.findAll();
        for(Loja lojaEnt:lojasEnt){
            LojaDTO loja = new LojaDTO();
            LojaDTO.Franquia franquia = new LojaDTO.Franquia();
            loja.setNome(lojaEnt.getNome());
            loja.setSystemId(lojaEnt.getSystemId());
            franquia.setNome(lojaEnt.getFranquia().getNome());
            franquia.setSystemId(lojaEnt.getFranquia().getSystemId());
            loja.setFranquia(franquia);
            output.add(loja);
        }
        return output;
    }
}
