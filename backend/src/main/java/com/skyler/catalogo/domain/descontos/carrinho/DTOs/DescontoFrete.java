package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

@Data
public class DescontoFrete {
    String systemId;
    Integer lowerValueLimitToApply;
    Float percentDecimalDiscount;
}
