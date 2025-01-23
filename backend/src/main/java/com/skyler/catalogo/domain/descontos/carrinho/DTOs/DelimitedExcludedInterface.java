package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

import java.util.List;

public interface DelimitedExcludedInterface {
    List<String> getDelimitedCategorias();
    List<String> getExcludedCategorias();
    List<String> getDelimitedLinhas();
    List<String> getExcludedLinhas();
    List<String> getDelimitedGrupos();
    List<String> getExcludedGrupos();
}
