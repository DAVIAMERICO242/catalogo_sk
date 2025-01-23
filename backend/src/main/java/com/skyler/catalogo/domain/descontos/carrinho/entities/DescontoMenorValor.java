package com.skyler.catalogo.domain.descontos.carrinho.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="discounts_menor_valor_cart")
public class DescontoMenorValor {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private Integer lowerQuantityLimitToApply;
    private Float percentDecimalDiscount;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discount_system_id")
    private Desconto desconto;
}

