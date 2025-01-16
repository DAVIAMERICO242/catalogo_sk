package com.skyler.catalogo.domain.produtos.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class AtributosDTO {
    List<String> categorias;
    List<String> modelagens;
    List<String> grupos;
}
