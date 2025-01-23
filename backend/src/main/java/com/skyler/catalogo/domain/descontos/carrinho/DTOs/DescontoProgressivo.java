package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class DescontoProgressivo {
    String systemId;
    List<String> delimitedCategorias;
    List<String> excludedCategorias;
    List<String> delimitedLinhas;
    List<String> excludedLinha;
    List<String> delimitedGrupos;
    List<String> excludedGrupos;
    List<IntervalDesconto> intervalos;
}
