package com.skyler.catalogo.domain.correios;

import lombok.Data;

@Data
public class CorreiosFranquiaDTO {
    private String systemId;
    private String franquiaId;
    private String numeroContrato;
    private String numeroDiretoriaRegional;
    private String usuario;
    private String senha;
    private String codigoPac;
    private String codigoSedex;
    private String cepOrigem;
}
