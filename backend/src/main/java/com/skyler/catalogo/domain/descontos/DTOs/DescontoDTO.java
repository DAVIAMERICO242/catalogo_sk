package com.skyler.catalogo.domain.descontos.DTOs;

import com.skyler.catalogo.domain.descontos.enums.DescontoTipo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    List<LojaDescontoDTO> lojas = new ArrayList<>();

    public void addLojaDesconto(LojaDescontoDTO loja){
        this.lojas.add(loja);
    }
}
