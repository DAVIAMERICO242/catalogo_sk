package com.skyler.catalogo.domain.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HowShouldBeCalculatedResponse {
    TipoCalculoEnum tipo;
}
