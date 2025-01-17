package com.skyler.catalogo.infra.integrador;

import lombok.Data;

import java.util.List;

@Data
public class IntegradorEstoque{
    String produtoIntegradorId;
    String lojaIntegradorId;
    String sku;
    List<Variacao> variacoes;
    @Data
    public static class Variacao{
        String sku;
        Integer estoque;
    }
}
