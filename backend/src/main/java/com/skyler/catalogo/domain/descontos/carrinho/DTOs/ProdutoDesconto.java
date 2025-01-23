package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

@Data
public class ProdutoDesconto {
    String systemId;
    String nome;
    Float baseValue;
    Float catalogValue;
}
