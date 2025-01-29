package com.skyler.catalogo.domain.pedidos;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.descontos.entities.Desconto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name="descontos_aplicados")
@Data
@EqualsAndHashCode(of="system_id")
public class DescontosAplicados {
    @Id
    private String systemId = UUID.randomUUID().toString();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="desconto_id")
    @JsonBackReference
    private Desconto desconto;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    @JsonBackReference
    private Pedido pedido;
    private Float appliedValue;
}
