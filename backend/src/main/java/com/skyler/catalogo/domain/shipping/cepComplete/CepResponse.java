package com.skyler.catalogo.domain.shipping.cepComplete;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CepResponse {
    String rua;
    String bairro;
    String cidade;
    String estado;
}
