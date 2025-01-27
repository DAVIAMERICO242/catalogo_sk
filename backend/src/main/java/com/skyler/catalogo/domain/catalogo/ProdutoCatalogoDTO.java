package com.skyler.catalogo.domain.catalogo;

import com.skyler.catalogo.domain.produtos.DTOs.ProdutoDTO;
import lombok.Data;

@Data
public class ProdutoCatalogoDTO {
    String systemId;
    ProdutoDTO produtoBase;
    Loja lojaCatalogo;
    @Data
    public static class Loja{
        String systemId;
        String slug;
        String loja;
    }
}
