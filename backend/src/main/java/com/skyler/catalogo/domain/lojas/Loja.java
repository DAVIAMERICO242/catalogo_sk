package com.skyler.catalogo.domain.lojas;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.franquias.Franquia;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(of="systemId")
@Table(name="lojas")
public class Loja {
    @Id
    @Column(name="system_id")
    private String systemId = UUID.randomUUID().toString();
    @Column(name="loja_integrador_id")
    private String integradorId;
    private Integer erpId;
    private String nome;
    private String slug;
    private String endereco;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="franquia_system_id")
    @JsonBackReference
    private Franquia franquia;

}
