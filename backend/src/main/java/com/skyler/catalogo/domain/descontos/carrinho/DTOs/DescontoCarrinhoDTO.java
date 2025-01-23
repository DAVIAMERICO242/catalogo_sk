package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DescontoCarrinhoDTO {
    String systemId;
    Loja loja;
    String discountName;
    Boolean isActive;
    LocalDateTime expiresAt;
    Integer cartRequiredQuantity;
    Float totalCartValueDiscount;
    Float totalCartDecimalPercentDiscount;
    Float cheapestItemValueDiscount;
    Float cheapestItemDecimalPercentDiscount;
    Float expensiveItemValueDiscount;
    Float expensiveItemDecimalPercentDiscount;
    Produto bonusOutOfCartCatalogProduct;
    Float shippingValueDiscount;
    Float shippingDecimalPercentDiscount;
    List<String> delimitedTerms = new ArrayList<>();
    List<String> excludedTerms = new ArrayList<>();

    public void addDelimitedTerm(String term){
        this.delimitedTerms.add(term);
    }

    public void addExcludedTerm(String term){
        this.excludedTerms.add(term);
    }

    @Data
    public static class Loja{
        String nome;
        String systemId;
        String slug;
    }
    @Data
    public static class Produto{
        String nome;
        String sku;
        String systemId;
    }
}
