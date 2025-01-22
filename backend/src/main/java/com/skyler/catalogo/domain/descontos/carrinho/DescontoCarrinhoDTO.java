package com.skyler.catalogo.domain.descontos.carrinho;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DescontoCarrinhoDTO {
    String systemId;
    Loja loja;
    String discountName;
    String descriptionDelimitation;
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
