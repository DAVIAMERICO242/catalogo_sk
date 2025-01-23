package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import com.skyler.catalogo.domain.lojas.LojaDTO;
import lombok.Data;

public class DescontoSimples {
    String systemId;
    ProdutoDesconto produto;
    Float percentDecimalDiscount;
}
