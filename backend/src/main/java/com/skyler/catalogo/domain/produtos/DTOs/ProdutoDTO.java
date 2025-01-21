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
    String photoUrl;
    Boolean onCatalogo;

    @Data
    public static class Franquia{
        String franquiaSystemId;
        String franquia;
    }


}
