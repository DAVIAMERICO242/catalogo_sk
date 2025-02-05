package com.skyler.catalogo.domain.produtos.DTOs;

import com.skyler.catalogo.infra.integrador.DTOs.ProdutoIntegrador;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProdutoDTO {//as variações nem sempre são carregadas
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
    String photoUrl;
    Boolean onCatalogo;
    List<Variacao> variacoes = new ArrayList<>();

    public void addVariacao(Variacao variacao){
        this.variacoes.add(variacao);
    }
    
    @Data
    public static class Variacao{
        String systemId;
        String sku;
        String cor;
        String tamanho;
    }

    @Data
    public static class Franquia{
        String franquiaSystemId;
        String franquia;
    }


}
