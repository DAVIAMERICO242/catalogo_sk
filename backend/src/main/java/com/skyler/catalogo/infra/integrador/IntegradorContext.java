package com.skyler.catalogo.infra.integrador;

import org.springframework.beans.factory.annotation.Value;

public abstract class IntegradorContext {
    @Value("${app.integrador}")
    protected String integradorUrl;
    @Value("${app.update-lojas-and-franquias-on-construct}")
    protected Boolean updateLojasAndFranquiasOnConstruct;
    @Value("${app.update-produtos-on-construct}")
    protected Boolean updateProdutosOnConstruct;
}
