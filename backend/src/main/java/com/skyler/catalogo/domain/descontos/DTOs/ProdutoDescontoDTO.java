package com.skyler.catalogo.domain.descontos.DTOs;

import lombok.Data;

@Data
public class ProdutoDescontoDTO {
    String systemId;
    String nome;
    Float baseValue;
    Float catalogValue;
}
