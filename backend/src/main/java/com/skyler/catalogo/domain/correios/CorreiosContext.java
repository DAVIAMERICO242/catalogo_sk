package com.skyler.catalogo.domain.correios;


import org.springframework.beans.factory.annotation.Value;

public abstract class CorreiosContext {

    @Value("${app.correios-login}")
    static String login;
    @Value("${app.correios-password}")
    static String password;
    @Value("${app.correios-codigo-pac}")
    static Integer codigoPac;
    @Value("${app.correios-codigo-sedex}")
    static Integer codigoSedex;

}
