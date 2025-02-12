package com.skyler.catalogo.domain.pedidos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name="orders")
@EqualsAndHashCode(of="systemId")
public class Pedido {
    @Id
    @Column(name="system_id")
    private String systemId = UUID.randomUUID().toString();
    private LocalDateTime moment = LocalDateTime.now();
    private String documento;
    private String nome;
    private Integer numero;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private Boolean entregaLoja;
    private String cep;
    private String telefone;
    private Float valor;
    @Enumerated(EnumType.STRING)
    private FreteEnum tipoFrete;//so existe se entrega loja nao for true
    private Float valorFrete;
    private Boolean pago = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="loja_id")
    @JsonBackReference
    private Loja loja;

    @ManyToMany
    @JoinTable(name = "orders_products",
            joinColumns = @JoinColumn(name="order_id",referencedColumnName = "system_id"),
            inverseJoinColumns = @JoinColumn(name="variacao_id",referencedColumnName = "system_id")
    )
    @JsonBackReference
    private Set<ProdutoVariacao> produtos = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "pedido")
    @JsonManagedReference
    private Set<DescontosAplicados> descontosAplicados = new HashSet<>();

    public void addProduto(ProdutoVariacao produtoVariacao){
        this.produtos.add(produtoVariacao);
    }

    public void addDescontoAplicado(DescontosAplicados descontoAplicado){
        descontoAplicado.setPedido(this);
        this.descontosAplicados.add(descontoAplicado);
    }


}
