package com.skyler.catalogo.domain.descontos.carrinho.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Lazy;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name="discounts_progressive")
public class DescontoProgressivo {
    @Id
    private String systemId = UUID.randomUUID().toString();
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discount_system_id")
    private Desconto desconto;
    @OneToMany(fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL,mappedBy = "descontoProgressivo")
    private Set<DescontoProgressivoIntervalos> intervalos = new HashSet<>();
}
