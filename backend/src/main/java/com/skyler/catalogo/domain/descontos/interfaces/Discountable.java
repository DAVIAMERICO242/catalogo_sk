package com.skyler.catalogo.domain.descontos.interfaces;

import com.skyler.catalogo.domain.pedidos.DTOs.LojaPedidoDTO;
import com.skyler.catalogo.domain.pedidos.DTOs.ProdutoPedidoDTO;

import java.util.List;

public interface Discountable {

    Float getValorFrete();
    List<ProdutoPedidoDTO> getProdutos();
    LojaPedidoDTO getLoja();
}
