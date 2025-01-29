package com.skyler.catalogo.domain.carrinho;

import com.skyler.catalogo.domain.descontos.interfaces.Discountable;
import com.skyler.catalogo.domain.pedidos.DTOs.LojaPedidoDTO;
import com.skyler.catalogo.domain.pedidos.DTOs.ProdutoPedidoDTO;
import lombok.Data;

import java.util.List;

@Data
public class CarrinhoRequest implements Discountable {
    LojaPedidoDTO loja;
    List<ProdutoPedidoDTO> produtos;
    Float valorFrete = 0f;
}
