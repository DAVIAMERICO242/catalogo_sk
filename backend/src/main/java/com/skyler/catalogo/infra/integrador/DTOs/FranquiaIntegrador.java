package com.skyler.catalogo.infra.integrador.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FranquiaIntegrador {
    private String integradorId;
    private String erpToken;
    private String cnpj;
    private String nome;
    private Boolean isMatriz;
    private List<Loja> lojas;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Loja{
        private String integradorId;
        private Integer erpId;
        private String nome;
    }
}
