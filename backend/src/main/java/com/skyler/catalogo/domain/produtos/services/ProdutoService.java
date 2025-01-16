package com.skyler.catalogo.domain.produtos.services;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import com.skyler.catalogo.domain.produtos.DTOs.AtributosDTO;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FranquiaRepository franquiaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, FranquiaRepository franquiaRepository) {
        this.produtoRepository = produtoRepository;
        this.franquiaRepository = franquiaRepository;
    }

    public AtributosDTO getAtributos(String franquiaSystemId){
        Optional<Franquia> franquiaOPT = this.franquiaRepository.findById(franquiaSystemId);
        if(franquiaOPT.isEmpty()){
            throw new RuntimeException("Franquia não encontrada");
        }
        Franquia franquia = franquiaOPT.get();
        AtributosDTO atributos = new AtributosDTO();
        atributos.setCategorias(this.produtoRepository.getCategorias(franquia));
        atributos.setModelagens(this.produtoRepository.getModelagens(franquia));
        atributos.setGrupos(this.produtoRepository.getGrupos(franquia));
        return atributos;
    }
}
