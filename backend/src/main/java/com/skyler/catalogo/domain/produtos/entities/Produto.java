package com.skyler.catalogo.domain.produtos.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skyler.catalogo.domain.franquias.Franquia;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="produtos")
@Data
@EqualsAndHashCode(of="systemId")
public class Produto {
    @Id
    private String systemId = UUID.randomUUID().toString();
    @Column(name="integrador_id")
    private String produtoIntegradorId;
    private Integer erpId;
    private String sku;
    private String descricao;
    private String categoria;
    private String modelagem;
    private String unidade;
    private String linha;
    private String colecao;
    private String tipo;
    private String grupo;
    private String subgrupo;
    private Float preco;
    private String photoUrl;
    private Boolean wasPhotoChangedManually = false;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "produto",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    @OrderBy("skuPonto ASC")
    private Set<ProdutoVariacao> variacoes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="franquia_system_id")
    @JsonBackReference
    private Franquia franquia;

    public void addOrUpdateVariacao(ProdutoVariacao variacao){
        Optional<ProdutoVariacao> existingOPT = variacoes.stream().filter(o->o.getSkuPonto().equals(variacao.getSkuPonto())).findFirst();
        if(existingOPT.isEmpty()){
            variacao.setProduto(this);
            variacoes.add(variacao);
        }else{
            ProdutoVariacao variacaoEnt = existingOPT.get();
            variacaoEnt.setCor(variacao.getCor());
            variacaoEnt.setTamanho(variacao.getTamanho());
            variacaoEnt.setFotoUrl(variacao.getFotoUrl());        }
    }
}
