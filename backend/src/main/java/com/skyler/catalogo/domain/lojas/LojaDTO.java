package com.skyler.catalogo.domain.lojas;

import lombok.Data;

@Data
public class LojaDTO {
    String loja;
    String systemId;
    Franquia franquia;
    @Data
    public static class Franquia{
        String franquia;
        String systemId;
    }

}
