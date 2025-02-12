package com.skyler.catalogo.domain.shipping.pesoCategorias;

import lombok.Data;

@Data
public class PesoCategoriasRequest {
    String categoria;
    String franquiaId;
    Float pesoGramas;
}
