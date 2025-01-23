package com.skyler.catalogo.domain.descontos.carrinho.entities;

import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="discounts_simples")
public class DescontoSimplesProduto {
    @Id
    private String systemId = UUID.randomUUID().toString();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="catalog_product_system_id")
    private ProdutoCatalogo produtoCatalogo;
    private Float percentDecimalDiscount;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discount_system_id")
    private Desconto desconto;

}
