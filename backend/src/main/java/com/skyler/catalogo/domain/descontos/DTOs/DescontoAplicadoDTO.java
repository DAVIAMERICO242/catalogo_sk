package com.skyler.catalogo.domain.descontos.DTOs;

import com.skyler.catalogo.domain.descontos.enums.DescontoTipo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescontoAplicadoDTO {
    String systemId;
    String nome;
    DescontoTipo tipo;
    Float valorAplicado;
}
