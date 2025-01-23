package com.skyler.catalogo.domain.descontos.carrinho.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference
    private Desconto desconto;
    @OneToMany(fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL,mappedBy = "descontoProgressivo")
    @JsonManagedReference
    private Set<DescontoProgressivoIntervalos> intervalos = new HashSet<>();

    public void addInterval(DescontoProgressivoIntervalos intervalo){
        intervalo.setDescontoProgressivo(this);
        this.intervalos.add(intervalo);
    }
}
