package com.skyler.catalogo.domain.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreteResponseDTO {
    TipoCalculoEnum tipo;
    Float valorFaixaCep;
    Integer prazoEmDiasFaixaCep;
    Float valorPac;
    Integer prazoEmDiasPac;
    Float valorSedex;
    Integer prazoEmDiasSedex;
}
