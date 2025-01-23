package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class DescontoMenorValorDTO implements DelimitedExcludedInterface{
    String systemId;
    Integer lowerQuantityLimitToApply;
    Integer lowerValueLimitToApply;
    List<String> delimitedCategorias;
    List<String> excludedCategorias;
    List<String> delimitedLinhas;
    List<String> excludedLinhas;
    List<String> delimitedGrupos;
    List<String> excludedGrupos;
    Float percentDecimalDiscount;
}
