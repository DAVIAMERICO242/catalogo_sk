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
    DescontoFreteDTO descontoFrete;
    DescontoSimplesDTO descontoSimples;
    DescontoSimplesTermoDTO descontoSimplesTermo;
    DescontoGenericoCarrinhoDTO descontoGenericoCarrinho;
    DescontoMaiorValorDTO descontoMaiorValor;
    DescontoMenorValorDTO descontoMenorValor;
    DescontoProgressivoDTO descontoProgressivo;
    LojaDescontoDTO loja;
}
