package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class DescontoProgressivoDTO implements DelimitedExcludedInterface {
    String systemId;
    List<String> delimitedCategorias;
    List<String> excludedCategorias;
    List<String> delimitedLinhas;
    List<String> excludedLinhas;
    List<String> delimitedGrupos;
    List<String> excludedGrupos;
    List<IntervalDescontoDTO> intervalos;
}
