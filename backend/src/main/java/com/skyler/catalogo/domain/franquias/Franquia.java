package com.skyler.catalogo.domain.franquias;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skyler.catalogo.domain.lojas.Loja;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Lazy;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(of="systemId")
@Table(name="franquias")
public class Franquia {
    @Id
    private String systemId = UUID.randomUUID().toString();
    @Column(name="franquia_integrador_id")
    private String integradorId;
    private String cnpj;
    private String nome;
    private Boolean isMatriz;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE},mappedBy = "franquia")
    @JsonManagedReference
    private Set<Loja> lojas = new HashSet<>();
}
