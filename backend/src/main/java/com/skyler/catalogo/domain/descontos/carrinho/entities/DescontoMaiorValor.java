package com.skyler.catalogo.domain.descontos.carrinho.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Data
@Table(name="discounts_maior_valor_cart")
@EqualsAndHashCode(of = "systemId")
public class DescontoMaiorValor {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private Integer lowerQuantityLimitToApply;
    private Float percentDecimalDiscount;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discount_system_id")
    @JsonBackReference
    private Desconto desconto;
}
