package com.skyler.catalogo.domain.descontos.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skyler.catalogo.domain.descontos.enums.DescontoTipo;
import com.skyler.catalogo.domain.lojas.Loja;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
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
    @Column(name="system_id")
    private String systemId = UUID.randomUUID().toString();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "discounts_lojas",
            joinColumns = @JoinColumn(name="desconto_id",referencedColumnName = "system_id"),
            inverseJoinColumns = @JoinColumn(name="loja_id",referencedColumnName = "system_id")
    )
    @JsonBackReference
    private Set<Loja> lojas = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private DescontoTipo descontoTipo;
    private String discountName;
    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDate expiresAt;
    @OneToOne(mappedBy = "desconto",cascade = CascadeType.ALL)
    @JsonManagedReference
    private DescontoFrete descontoFrete;
    @OneToOne(mappedBy = "desconto",cascade = CascadeType.ALL)
    @JsonManagedReference
    private DescontoGenericoCarrinho descontoGenericoCarrinho;
    @OneToOne(mappedBy = "desconto",cascade = CascadeType.ALL)
    @JsonManagedReference
    private DescontoMaiorValor descontoMaiorValor;
    @OneToOne(mappedBy = "desconto",cascade = CascadeType.ALL)
    @JsonManagedReference
    private DescontoMenorValor descontoMenorValor;
    @OneToOne(mappedBy = "desconto",cascade = CascadeType.ALL)
    @JsonManagedReference
    private DescontoProgressivo descontoProgressivo;
    @OneToOne(mappedBy = "desconto",cascade = CascadeType.ALL)
    @JsonManagedReference
    private DescontoSimplesProduto descontoSimplesProduto;
    @OneToOne(mappedBy = "desconto",cascade = CascadeType.ALL)
    @JsonManagedReference
    private DescontoSimplesTermo descontoSimplesTermo;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "desconto")
    @JsonManagedReference
    private Set<DelimitedTermos> delimitedTermos = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "desconto")
    @JsonManagedReference
    private Set<ExcludedTermos> excludedTermos = new HashSet<>();

    public void addLoja(Loja loja){
        this.lojas.add(loja);
    }

    public void addDelimitedTermo(DelimitedTermos termo){
        termo.setDesconto(this);
        this.delimitedTermos.add(termo);
    }

    public void addExcludedTermo(ExcludedTermos termo){
        termo.setDesconto(this);
        this.excludedTermos.add(termo);
    }
}
