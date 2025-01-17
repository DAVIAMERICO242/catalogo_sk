package com.skyler.catalogo.domain.produtos.DTOs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProdutoEstoqueDTO {
    String sku;
    List<VariacaoEstoque> estoque = new ArrayList<>();
    @Data
    public static class VariacaoEstoque{
        String sku;
        Integer estoque;
    }
    public void addVariacao(VariacaoEstoque estoque){
        this.estoque.add(estoque);
    }
}
