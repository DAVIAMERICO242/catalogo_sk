package com.skyler.catalogo.domain.shipping.faixasCep;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.franquias.Franquia;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(of="systemId")
@Table(name="faixa_cep_precos")
public class ShippingRules {

    @Id
    private String systemId = UUID.randomUUID().toString();
    private String cepInicio;
    private String cepFim;
    private Float minValueToApply;
    private Float valorFixo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="franquia_id")
    @JsonBackReference
    private Franquia franquia;
}
