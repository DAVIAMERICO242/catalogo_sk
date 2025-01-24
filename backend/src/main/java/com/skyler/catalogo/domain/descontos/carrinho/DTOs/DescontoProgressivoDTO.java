package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class DescontoProgressivoDTO implements DelimitedExcludedInterface {
    String systemId;
    List<String> delimitedCategorias;
    List<String> excludedCategorias;
    List<String> delimitedLinhas;
    List<String> excludedLinhas;
    List<String> delimitedGrupos;
    List<String> excludedGrupos;
    List<IntervalDescontoDTO> intervalos;

    public void addIntervalo(IntervalDescontoDTO intervalDescontoDTO){
        this.intervalos.add(intervalDescontoDTO);
    }

    @Override
    public void addDelimitedCategoria(String termo) {
        this.delimitedCategorias.add(termo);
    }

    @Override
    public void addDelimitedLinha(String termo) {
        this.delimitedLinhas.add(termo);
    }

    @Override
    public void addDelimitedGrupo(String termo) {
        this.delimitedGrupos.add(termo);
    }

    @Override
    public void addExcudedCategoria(String termo) {
        this.excludedCategorias.add(termo);
    }

    @Override
    public void addExcludedLinha(String termo) {
        this.excludedLinhas.add(termo);
    }

    @Override
    public void addExcludedGrupo(String termo) {
        this.excludedGrupos.add(termo);
    }
}
