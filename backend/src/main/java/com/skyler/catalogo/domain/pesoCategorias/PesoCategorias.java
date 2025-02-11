package com.skyler.catalogo.domain.pesoCategorias;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.franquias.Franquia;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name="categorias_pesos")
@Data
public class PesoCategorias {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private String categoria;
    private Float pesoGramas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="franquia_id")
    private Franquia franquia;
}
