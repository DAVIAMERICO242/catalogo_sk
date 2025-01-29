package com.skyler.catalogo.domain.descontos.DTOs;

import lombok.Data;

@Data
public class DescontoSimplesDTO {
    String systemId;
    ProdutoDescontoDTO produto;
    Float percentDecimalDiscount;
}
