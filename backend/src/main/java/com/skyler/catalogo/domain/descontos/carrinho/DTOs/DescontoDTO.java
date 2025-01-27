package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DescontoDTO {
    String systemId;
    String nome;
    DescontoTipo tipo;
    LocalDate expiresAt;
    LocalDateTime createdAt;
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
