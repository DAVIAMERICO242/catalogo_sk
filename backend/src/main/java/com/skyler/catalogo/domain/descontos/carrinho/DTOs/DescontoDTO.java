package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DescontoDTO {
    String systemId;
    DescontoTipo tipo;
    LocalDateTime expiresAt;
    Boolean isActive;
    DescontoFrete descontoFrete;
    DescontoSimples descontoSimples;
    DescontoSimplesTermo descontoSimplesTermo;
    DescontoGenericoCarrinho descontoGenericoCarrinho;
    DescontoMaiorValor descontoMaiorValor;
    DescontoMenorValor descontoMenorValor;
    DescontoProgressivo descontoProgressivo;
    LojaDesconto loja;
}
