package com.skyler.catalogo.domain.descontos.carrinho.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.descontos.carrinho.enums.TermoTipo;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="discounts_excluded_termos")
public class ExcludedTermos {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private String termo;
    @Enumerated(EnumType.STRING)
    private TermoTipo classificacaoTermo;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="discount_system_id")
    private Desconto desconto;

}
