package com.skyler.catalogo.domain.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreteResponseDTO {
    Float valor;
    Integer prazoEmDias;
}
