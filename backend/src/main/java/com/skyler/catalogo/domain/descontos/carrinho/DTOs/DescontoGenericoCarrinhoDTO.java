package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

@Data
public class DescontoGenericoCarrinhoDTO {
    String systemId;
    Float minValue;
    Float percentDecimalDiscount;
}
