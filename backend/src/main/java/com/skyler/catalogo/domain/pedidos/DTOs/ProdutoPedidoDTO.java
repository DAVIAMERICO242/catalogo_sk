package com.skyler.catalogo.domain.pedidos.DTOs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProdutoPedidoDTO {
    String systemId;
    String sku;
    String nome;
    Float valorBase;
    List<ProdutoVariacao> variacoesCompradas = new ArrayList<>();
    public void addVariacao(ProdutoVariacao variacaoComprada){
        this.variacoesCompradas.add(variacaoComprada);
    }
    @Data
    public static class ProdutoVariacao{//nao unico
        String systemId;
        String sku;
        String cor;
        String tamanho;
        Float valorBase;
        String fotoUrl;
    }
}
