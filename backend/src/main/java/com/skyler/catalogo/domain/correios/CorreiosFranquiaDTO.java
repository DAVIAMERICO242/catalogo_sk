package com.skyler.catalogo.domain.correios;

import lombok.Data;

@Data
public class CorreiosFranquiaDTO {
    private String systemId;
    private String franquiaId;
    private String usuario;
    private String senha;
    private Integer codigoPac;
    private Integer codigoSedex;
    private String cepOrigem;
}
