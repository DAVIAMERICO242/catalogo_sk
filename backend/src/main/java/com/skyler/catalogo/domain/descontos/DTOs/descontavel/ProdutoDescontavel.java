package com.skyler.catalogo.domain.descontos.DTOs.descontavel;

import com.skyler.catalogo.domain.descontos.interfaces.Discountable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProdutoDescontavel {
    String systemId;
    String sku;
    String nome;
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
        String fotoUrl;
    }
}
