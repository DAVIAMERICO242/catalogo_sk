package com.skyler.catalogo.domain.descontos.carrinho.DTOs;

import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescontoAplicadoDTO {
    String systemId;
    String nome;
    DescontoTipo tipo;
    Float valorAplicado;
}
