package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import com.skyler.catalogo.domain.lojas.Loja;

import java.util.List;

public class DescontoSimplesTermo {
    String systemId;
    List<String> delimitedCategorias;
    List<String> excludedCategorias;
    List<String> delimitedLinhas;
    List<String> excludedLinha;
    List<String> delimitedGrupos;
    List<String> excludedGrupos;
    Float percentDecimalDiscount;
}
