package com.skyler.catalogo.domain.pesoCategorias;

import lombok.Data;

@Data
public class PesoCategoriasDTO {
    String systemId;
    String franquiaId;
    String categoria;
    Float pesoGramas;
}
