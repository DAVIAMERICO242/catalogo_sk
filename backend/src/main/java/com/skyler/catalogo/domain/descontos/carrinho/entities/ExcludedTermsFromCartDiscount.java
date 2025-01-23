package com.skyler.catalogo.domain.descontos.carrinho.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="excluded_terms_from_cart_discount")
public class ExcludedTermsFromCartDiscount {
    @Id
    private String systemId = UUID.randomUUID().toString();
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="cart_discount_system_id")
    private DescontoCarrinho descontoCarrinho;
    private String excludedTerm;//
}
