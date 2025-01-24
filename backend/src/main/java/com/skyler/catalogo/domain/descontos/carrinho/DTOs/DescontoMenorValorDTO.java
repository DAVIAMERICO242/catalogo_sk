package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class DescontoMenorValorDTO implements DelimitedExcludedInterface{
    String systemId;
    Integer lowerQuantityLimitToApply;
    List<String> delimitedCategorias;
    List<String> excludedCategorias;
    List<String> delimitedLinhas;
    List<String> excludedLinhas;
    List<String> delimitedGrupos;
    List<String> excludedGrupos;
    Float percentDecimalDiscount;

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
