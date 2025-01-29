package com.skyler.catalogo.domain.descontos.interfaces;

import java.util.List;

public interface DelimitedExcludedInterface {
    List<String> getDelimitedCategorias();
    List<String> getExcludedCategorias();
    List<String> getDelimitedLinhas();
    List<String> getExcludedLinhas();
    List<String> getDelimitedGrupos();
    List<String> getExcludedGrupos();
    void addDelimitedCategoria(String termo);
    void addDelimitedLinha(String termo);
    void addDelimitedGrupo(String termo);
    void addExcudedCategoria(String termo);
    void addExcludedLinha(String termo);
    void addExcludedGrupo(String termo);
}
