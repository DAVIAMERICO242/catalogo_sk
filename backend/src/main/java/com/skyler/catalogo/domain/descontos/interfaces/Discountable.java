package com.skyler.catalogo.domain.descontos.interfaces;

import com.skyler.catalogo.domain.descontos.DTOs.descontavel.LojaDescontavel;
import com.skyler.catalogo.domain.descontos.DTOs.descontavel.ProdutoDescontavel;

import java.util.List;

public interface Discountable {

    Float getValorFrete();
    List<ProdutoDescontavel> getProdutos();
    LojaDescontavel getLoja();
}
