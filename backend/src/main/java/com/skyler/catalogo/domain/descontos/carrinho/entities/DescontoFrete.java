package com.skyler.catalogo.domain.descontos.carrinho.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Entity
@Table(name="discounts_shipping")
@EqualsAndHashCode(of = "systemId")
public class DescontoFrete {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private Float lowerValueLimitToApply;
    private Float percentDecimalDiscount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discount_system_id")
    @JsonBackReference
    private Desconto desconto;
}
