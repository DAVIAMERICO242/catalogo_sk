package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

@Data
public class DescontoGenericoCarrinho {
    String systemId;
    Float minValue;
    Float percentDecimalDiscount;
}
