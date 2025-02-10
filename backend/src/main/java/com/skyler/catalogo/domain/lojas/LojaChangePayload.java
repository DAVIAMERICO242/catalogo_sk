package com.skyler.catalogo.domain.lojas;

import lombok.Data;

@Data
public class LojaChangePayload {
    String systemId;
    String endereco;
    String cep;
    String telefone;
}
