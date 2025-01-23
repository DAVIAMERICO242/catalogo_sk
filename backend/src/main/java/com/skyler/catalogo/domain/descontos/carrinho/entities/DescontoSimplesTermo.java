package com.skyler.catalogo.domain.descontos.carrinho.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="discounts_simples_termo")
public class DescontoSimplesTermo {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private Float percentDecimalDiscount;
    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="discount_system_id")
    private Desconto desconto;
}
