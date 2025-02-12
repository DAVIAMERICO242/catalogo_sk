package com.skyler.catalogo.domain.shipping;

import com.skyler.catalogo.domain.descontos.interfaces.Discountable;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.pedidos.DTOs.LojaPedidoDTO;
import com.skyler.catalogo.domain.pedidos.DTOs.ProdutoPedidoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ShippingDiscountable implements Discountable {
    List<ProdutoPedidoDTO> produtos;
    LojaPedidoDTO loja;
    Float valorFrete = 0f;
}
