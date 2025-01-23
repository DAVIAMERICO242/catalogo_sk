package com.skyler.catalogo.domain.descontos.carrinho.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="discounts_progressive_intervals")
public class DescontoProgressivoIntervalos {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private Integer minQuantity;
    private Float percentDecimalDiscount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discount_progressive_system_id")
    private DescontoProgressivo descontoProgressivo;

}
