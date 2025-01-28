package com.skyler.catalogo.domain.catalogo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Data
@Table(name="produtos_catalogo")
@EqualsAndHashCode(of="systemId")
public class ProdutoCatalogo {
    @Id
    @Column(name="system_id")
    private String systemId = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name="produto_system_id")
    @JsonBackReference
    private Produto produtoBaseFranquia;//o produto original da base desse sistema é a nivel de franquia, o produto catalogo a nivel de loja por isso many to one
    @OneToOne
    @JoinColumn(name="loja_system_id")
    @JsonBackReference
    private Loja loja;
}
