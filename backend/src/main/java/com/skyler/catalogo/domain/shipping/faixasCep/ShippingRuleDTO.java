package com.skyler.catalogo.domain.shipping.faixasCep;


import lombok.Data;

@Data
public class ShippingRuleDTO {
    private String systemId;
    private String franquiaId;
    private String cepInicio;
    private String cepFim;
    private Float minValueToApply;
    private Float valorFixo;
}
