package com.skyler.catalogo.domain.pedidos;

import com.skyler.catalogo.domain.pedidos.DTOs.LojaPedidoDTO;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

public interface BasicOrder {
    LojaPedidoDTO getLoja();
    LocalDateTime getMoment();
    String getDocumento();
    String getNome();
    Integer getNumero();
    String getRua();
    String getBairro();
    String getCidade();
    String getEstado();
    String getCep();
    String getTelefone();
    Float getValorFrete();

}
