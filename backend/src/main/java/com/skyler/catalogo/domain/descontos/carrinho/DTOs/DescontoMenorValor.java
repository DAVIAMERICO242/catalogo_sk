package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import java.util.List;

public class DescontoMenorValor {
    String systemId;
    Integer lowerQuantityLimitToApply;
    Integer lowerValueLimitToApply;
    List<String> delimitedCategorias;
    List<String> excludedCategorias;
    List<String> delimitedLinhas;
    List<String> excludedLinha;
    List<String> delimitedGrupos;
    List<String> excludedGrupos;
    Float percentDecimalDiscount;
}
