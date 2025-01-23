package com.skyler.catalogo.domain.descontos.carrinho.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import com.skyler.catalogo.domain.lojas.Loja;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Lazy;

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
    @Enumerated(EnumType.STRING)
    private DescontoTipo descontoTipo;
    private String discountName;
    private Boolean isActive = true;
    private Boolean isDiscountCumulative = false;
    private Boolean isIntervalCumulative = false;
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="single_product_catalog_id")
    @JsonBackReference
    private ProdutoCatalogo relatedProduct;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true,mappedBy = "descontoCarrinho")
    private Set<DescontoIntervalDetails> intervalDetails;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "descontoCarrinho",fetch = FetchType.LAZY,orphanRemoval = true)
    @JsonManagedReference
    private Set<DelimitedTermsFromCartDiscount> delimitedTerms = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "descontoCarrinho",fetch = FetchType.LAZY,orphanRemoval = true)
    @JsonManagedReference
    private Set<ExcludedTermsFromCartDiscount> excludedTerms = new HashSet<>();


    public void addDelimitedTerm(DelimitedTermsFromCartDiscount term){
        term.setDescontoCarrinho(this);
        this.delimitedTerms.add(term);
    }

    public void addExcludedTerm(ExcludedTermsFromCartDiscount term){
        term.setDescontoCarrinho(this);
        this.excludedTerms.add(term);
    }
}
