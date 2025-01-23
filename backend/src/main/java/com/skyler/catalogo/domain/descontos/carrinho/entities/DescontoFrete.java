package com.skyler.catalogo.domain.descontos.carrinho.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="discounts_shipping")
public class DescontoFrete {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private Float lowerValueLimitToApply;
    private Float percentDecimalDiscount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discount_system_id")
    private Desconto desconto;
}
