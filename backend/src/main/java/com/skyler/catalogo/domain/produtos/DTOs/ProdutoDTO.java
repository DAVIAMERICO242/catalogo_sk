package com.skyler.catalogo.domain.produtos.DTOs;

import com.skyler.catalogo.infra.integrador.DTOs.ProdutoIntegrador;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProdutoDTO {
    String systemId;
    Franquia franquia;
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
    List<Variacao> variacoes = new ArrayList<>();

    @Data
    public static class Franquia{
        String franquiaSystemId;
        String franquia;
    }

    @Data
    public static class Variacao{
        String systemId;
        Integer erpId;
        String sku;
        String cor;
        String tamanho;
        String photoUrl;
    }

    public void addVariacao(Variacao variacao){
        this.variacoes.add(variacao);
    }
}
