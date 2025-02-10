package com.skyler.catalogo.domain.lojas;

import lombok.Data;

@Data
public class LojaDTO {
    String loja;
    String slug;
    String systemId;
    String endereco;
    String telefone;
    String cep;
    Franquia franquia;
    @Data
    public static class Franquia{
        String franquia;
        String systemId;
    }

}
