package com.skyler.catalogo.domain.correios;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.franquias.Franquia;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Entity
@Data
@Table(name="correios_franquias")
public class CorreiosFranquiaContext {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private String usuario;
    private String senha;
    private Integer codigoPac;
    private Integer codigoSedex;
    private String cepOrigem;

    @OneToOne
    @JoinColumn(name="franquia_id")
    @JsonBackReference
    private Franquia franquia;
}
