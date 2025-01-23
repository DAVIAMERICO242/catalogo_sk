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

@Table(name="discounts")
@Data
@Entity
@EqualsAndHashCode(of="systemId")
public class Desconto{

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
    @OneToOne(mappedBy = "desconto")
    @JsonManagedReference
    private DescontoFrete descontoFrete;
    @OneToOne(mappedBy = "desconto")
    @JsonManagedReference
    private DescontoGenericoCarrinho descontoGenericoCarrinho;
    @OneToOne(mappedBy = "desconto")
    @JsonManagedReference
    private DescontoMaiorValor descontoMaiorValor;
    @OneToOne(mappedBy = "desconto")
    @JsonManagedReference
    private DescontoMenorValor descontoMenorValor;
    @OneToOne(mappedBy = "desconto")
    @JsonManagedReference
    private DescontoProgressivo descontoProgressivo;
    @OneToOne(mappedBy = "desconto")
    @JsonManagedReference
    private DescontoSimplesProduto descontoSimplesProduto;
    @OneToOne(mappedBy = "desconto")
    @JsonManagedReference
    private DescontoSimplesTermo descontoSimplesTermo;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "desconto")
    @JsonManagedReference
    private Set<DelimitedTermos> delimitedTermos;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "desconto")
    @JsonManagedReference
    private Set<ExcludedTermos> excludedTermos;
}
