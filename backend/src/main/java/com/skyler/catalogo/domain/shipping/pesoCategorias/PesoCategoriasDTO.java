package com.skyler.catalogo.domain.shipping.pesoCategorias;

import lombok.Data;

@Data
public class PesoCategoriasDTO {
    String systemId;
    String franquiaId;
    String categoria;
    Float pesoGramas;
}
