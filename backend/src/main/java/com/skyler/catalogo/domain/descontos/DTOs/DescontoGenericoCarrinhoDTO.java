package com.skyler.catalogo.domain.descontos.DTOs;

import lombok.Data;

@Data
public class DescontoGenericoCarrinhoDTO {
    String systemId;
    Float minValue;
    Float percentDecimalDiscount;
}
