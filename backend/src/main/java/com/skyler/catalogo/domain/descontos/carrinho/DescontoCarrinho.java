package com.skyler.catalogo.domain.descontos.carrinho;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.descontos.carrinho.terms.DelimitedTermsFromCartDiscount;
import com.skyler.catalogo.domain.descontos.carrinho.terms.ExcludedTermsFromCartDiscount;
import com.skyler.catalogo.domain.lojas.Loja;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name="cart_discounts")
@Data
@Entity
@EqualsAndHashCode(of="systemId")
public class DescontoCarrinho {

    @Id
    private String systemId = UUID.randomUUID().toString();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="loja_system_id")
    @JsonBackReference
    private Loja loja;
    private String discountName;
    private Boolean isActive = true;
    private LocalDateTime expiresAt;
    private Integer cartRequiredQuantity;
    private Float totalCartValueDiscount;
    private Float totalCartDecimalPercentDiscount;
    private Float cheapestItemValueDiscount;
    private Float cheapestItemDecimalPercentDiscount;
    private Float expensiveItemValueDiscount;
    private Float expensiveItemDecimalPercentDiscount;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "descontoCarrinho",fetch = FetchType.LAZY,orphanRemoval = true)
    @JsonManagedReference
    private Set<DelimitedTermsFromCartDiscount> delimitedTerms = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "descontoCarrinho",fetch = FetchType.LAZY,orphanRemoval = true)
    @JsonManagedReference
    private Set<ExcludedTermsFromCartDiscount> excludedTerms = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bonus_out_of_cart_catalog_product_id")
    @JsonBackReference
    private ProdutoCatalogo bonusOutOfCartCatalogProduct;

    private Float shippingValueDiscount;
    private Float shippingDecimalPercentDiscount;

    public void addDelimitedTerm(DelimitedTermsFromCartDiscount term){
        term.setDescontoCarrinho(this);
        this.delimitedTerms.add(term);
    }

    public void addExcludedTerm(ExcludedTermsFromCartDiscount term){
        term.setDescontoCarrinho(this);
        this.excludedTerms.add(term);
    }
}
