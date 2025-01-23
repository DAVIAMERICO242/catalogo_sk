package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class DescontoMaiorValorDTO implements DelimitedExcludedInterface {
    String systemId;
    Integer lowerQuantityLimitToApply;
    List<String> delimitedCategorias;
    List<String> excludedCategorias;
    List<String> delimitedLinhas;
    List<String> excludedLinhas;
    List<String> delimitedGrupos;
    List<String> excludedGrupos;
    Float percentDecimalDiscount;
}
