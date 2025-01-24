package com.skyler.catalogo.domain.produtos.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class TermoDTO {
    List<String> categorias;
    List<String> linhas;
    List<String> grupos;
}
