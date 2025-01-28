package com.skyler.catalogo.domain.pedidos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.descontos.carrinho.entities.Desconto;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.mapping.Join;
import org.springframework.context.annotation.Lazy;

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
    private LocalDateTime moment;
    private String documento;
    private String nome;
    private Integer numero;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefone;
    private Float valor;
    private Float valorFrete;
    private Boolean pago;

    @ManyToMany
    @JoinTable(name = "orders_products",
            joinColumns = @JoinColumn(name="order_id",referencedColumnName = "system_id"),
            inverseJoinColumns = @JoinColumn(name="variacao_id",referencedColumnName = "system_id")
    )
    @JsonBackReference
    private Set<ProdutoVariacao> produtos = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "orders_discounts",
            joinColumns = @JoinColumn(name="order_id",referencedColumnName = "system_id"),
            inverseJoinColumns = @JoinColumn(name="discount_id",referencedColumnName = "system_id")
    )
    @JsonBackReference
    private Set<Desconto> descontos = new HashSet<>();

    public void addProduto(ProdutoVariacao produtoVariacao){
        this.produtos.add(produtoVariacao);
    }

    public void addDesconto(Desconto desconto){
        this.descontos.add(desconto);
    }

}
