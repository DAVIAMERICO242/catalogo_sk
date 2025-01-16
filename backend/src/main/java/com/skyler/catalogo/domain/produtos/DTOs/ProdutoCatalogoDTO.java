package com.skyler.catalogo.domain.produtos.DTOs;

import lombok.Data;

@Data
public class ProdutoCatalogoDTO {
    ProdutoDTO produtoBase;
    Loja lojaCatalogo;
    Float valorCatalogo;
    @Data
    public static class Loja{
        String systemId;
        String slug;
        String loja;
    }
}
