package com.skyler.catalogo.domain.shipping.cepComplete;

import lombok.Data;

@Data
public class CepThirdApiResponse {
    String estado;
    String localidade;//cidade
    String logradouro;//rua
    String bairro;
}
