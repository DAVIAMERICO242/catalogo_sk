package com.skyler.catalogo.infra.integrador;

import org.springframework.beans.factory.annotation.Value;

public abstract class IntegradorContext {
    @Value("${app.integrador}")
    protected String integradorUrl;
}
