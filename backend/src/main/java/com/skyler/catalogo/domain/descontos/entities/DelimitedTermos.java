package com.skyler.catalogo.domain.descontos.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.descontos.enums.TermoTipo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Entity
@Table(name="discounts_delimited_termos")
@EqualsAndHashCode(of = "systemId")
public class DelimitedTermos {
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
