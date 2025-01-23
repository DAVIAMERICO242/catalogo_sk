package com.skyler.catalogo.domain.descontos.carrinho.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="cart_target_promotion_products")
public class TargetProdutctsIntervalReachPromotion {
    @Id
    private String systemId = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_discount_interval_system_id")
    @JsonBackReference
    private DescontoIntervalDetails descontoIntervalDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_catalog_id")
    @JsonBackReference
    private ProdutoCatalogo produtoCatalogo;
}
