package com.skyler.catalogo.domain.descontos.carrinho.entities;

import com.skyler.catalogo.domain.descontos.carrinho.enums.TermoTipo;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="discounts_delimited_termos")
public class DelimitedTermos {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private String termo;
    @Enumerated(EnumType.STRING)
    private TermoTipo classificacaoTermo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discount_system_id")
    private Desconto desconto;
}
