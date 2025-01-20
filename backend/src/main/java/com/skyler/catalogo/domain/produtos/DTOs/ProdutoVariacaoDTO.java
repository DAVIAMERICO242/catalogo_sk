package com.skyler.catalogo.domain.produtos.DTOs;

import lombok.Data;

@Data
public class ProdutoVariacaoDTO {
    String systemId;
    String integradorId;
    String sku;
    Integer erpId;
    String cor;
    String tamanho;
    String foto;
}
