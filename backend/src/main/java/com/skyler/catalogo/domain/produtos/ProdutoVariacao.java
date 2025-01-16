package com.skyler.catalogo.domain.produtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name="variacoes_produto")
@Data
@EqualsAndHashCode(of="systemId")
public class ProdutoVariacao {
    @Id
    private String systemId = UUID.randomUUID().toString();
    @Column(name="integrador_id")
    private String produtoVariacaoIntegradorId;
    private Integer erpId;
    private String skuPonto;//UNICO
    private String cor;
    private String tamanho;
    private String fotoUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="produto_system_id")
    @JsonBackReference
    private Produto produto;
}
