package com.skyler.catalogo.domain.descontos.DTOs;

import lombok.Data;

@Data
public class DescontoFreteDTO {
    String systemId;
    Float lowerValueLimitToApply;
    Float percentDecimalDiscount;
}
