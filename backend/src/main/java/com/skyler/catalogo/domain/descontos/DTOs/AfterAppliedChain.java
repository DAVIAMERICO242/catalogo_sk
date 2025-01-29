package com.skyler.catalogo.domain.descontos.DTOs;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AfterAppliedChain {
        List<DescontoAplicadoDTO> descontos;
        Float valorFinal;
}
