package com.skyler.catalogo.domain.lojas;

import lombok.Data;

@Data
public class LojaDTO {
    String loja;
    String slug;
    String systemId;
    Franquia franquia;
    String endereco;
    @Data
    public static class Franquia{
        String franquia;
        String systemId;
    }

}
