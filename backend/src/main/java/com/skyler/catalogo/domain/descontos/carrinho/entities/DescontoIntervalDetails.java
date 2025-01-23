package com.skyler.catalogo.domain.descontos.carrinho.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
import java.util.UUID;

@Table(name="cart_discounts_interval_distribution")
@Data
@Entity
@EqualsAndHashCode(of="systemId")
public class DescontoIntervalDetails {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private Integer lowerQuantityLimit;
    private Integer upperQuantityLimit;
    private Float lowerMoneyLimit;
    private Float upperMoneyLimit;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "descontoIntervalDetails")
    @JsonManagedReference
    private Set<TargetProdutctsIntervalReachPromotion> productsWithDiscountOnIntervalReach;
    @Column(name="target_promotion_beat_limit")
    private Integer allowedNumberOfAppliedDiscountsOnProductsWithDiscountOnIntervalReach = 1;
    @Column(name="target_promotion_value_discount")
    private Float baseDiscountValueForProductsReciviedDiscountOnIntervalReach;
    @Column(name="target_promotion_decimal_percent_discount")
    private Float baseDiscountDecimalPercentForProductsReciviedDiscountOnIntervalReach;
    private Float totalCartValueDiscount;
    private Float totalCartDecimalPercentDiscount;
    private Float cheapestItemValueDiscount;
    private Float cheapestItemDecimalPercentDiscount;
    private Float expensiveItemValueDiscount;
    private Float expensiveItemDecimalPercentDiscount;
    private Float shippingValueDiscount;
    private Float shippingDecimalPercentDiscount;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="cart_discount_system_id")
    private DescontoCarrinho descontoCarrinho;

}
