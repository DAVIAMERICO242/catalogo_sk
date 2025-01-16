package com.skyler.catalogo.infra.integrador.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class ProdutoIntegrador {
    String integradorId;
    String franquiaIntegradorId;
    Integer erpId;
    String sku;
    String descricao;
    String categoria;
    String unidade;
    String modelagem;
    String linha;
    String colecao;
    String tipo;
    String grupo;
    String subgrupo;
    Float preco;
    List<Variacao> variacoes;
    @Data
    public static class Variacao{
        String integradorId;
        Integer erpId;
        String sku;
        String cor;
        String tamanho;
        String photoUrl;
    }
}
