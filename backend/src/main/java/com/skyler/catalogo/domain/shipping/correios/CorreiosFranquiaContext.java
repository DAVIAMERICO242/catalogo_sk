package com.skyler.catalogo.domain.shipping.correios;


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
    private String numeroContrato;
    private String numeroCartaoPostal;
    private String numeroDiretoriaRegional;
    private String usuario;
    private String senha;
    private String codigoPac;
    private String codigoSedex;
    private String cepOrigem;

    @OneToOne
    @JoinColumn(name="franquia_id")
    @JsonBackReference
    private Franquia franquia;
}
