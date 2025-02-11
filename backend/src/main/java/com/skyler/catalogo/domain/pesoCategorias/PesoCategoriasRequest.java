package com.skyler.catalogo.domain.pesoCategorias;

import lombok.Data;

@Data
public class PesoCategoriasRequest {
    String categoria;
    String franquiaId;
    Float pesoGramas;
}
