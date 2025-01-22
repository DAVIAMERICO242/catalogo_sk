package com.skyler.catalogo.domain.descontos.carrinho;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.lojas.Loja;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name="cart_discounts")
@Data
@Entity
@EqualsAndHashCode(of="systemId")
public class DescontoCarrinho {

    @Id
    private String systemId = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name="loja_system_id")
    @JsonBackReference
    private Loja loja;
    private String discountName;
    private String descriptionDelimitation;
    private Boolean isActive;
    private LocalDateTime expiresAt;
    private Integer cartRequiredQuantity;
    private Float totalCartValueDiscount;
    private Float totalCartDecimalPercentDiscount;
    private Float cheapestItemValueDiscount;
    private Float cheapestItemDecimalPercentDiscount;
    private Float expensiveItemValueDiscount;
    private Float expensiveItemDecimalPercentDiscount;

    @ManyToOne
    @JoinColumn(name="bonus_out_of_cart_catalog_product_id")
    @JsonBackReference
    private ProdutoCatalogo bonusOutOfCartCatalogProduct;

    private Float shippingValueDiscount;
    private Float shippingDecimalPercentDiscount;

}
