package com.skyler.catalogo.domain.lojas;

import lombok.Data;

@Data
public class LojaDTO {
    String nome;
    String systemId;
    Franquia franquia;
    @Data
    public static class Franquia{
        String nome;
        String systemId;
    }

}
