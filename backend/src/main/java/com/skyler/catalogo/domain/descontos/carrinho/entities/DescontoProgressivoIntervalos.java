package com.skyler.catalogo.domain.descontos.carrinho.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Data
@Table(name="discounts_progressive_intervals")
@EqualsAndHashCode(of = "systemId")
public class DescontoProgressivoIntervalos {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private Integer minQuantity;
    private Float percentDecimalDiscount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="discount_progressive_system_id")
    private DescontoProgressivo descontoProgressivo;

}
