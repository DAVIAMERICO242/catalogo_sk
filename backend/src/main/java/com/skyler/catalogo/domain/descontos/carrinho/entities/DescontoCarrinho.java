package com.skyler.catalogo.domain.descontos.carrinho.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import com.skyler.catalogo.domain.lojas.Loja;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name="cart_discounts")
@Data
@Entity
@EqualsAndHashCode(of="systemId")
public class DescontoCarrinho {

    @Id
    private String systemId = UUID.randomUUID().toString();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="loja_system_id")
    @JsonBackReference
    private Loja loja;
    @Enumerated(EnumType.STRING)
    private DescontoTipo descontoTipo;
    private String discountName;
    private Boolean isActive = true;
    private LocalDateTime expiresAt;

}
