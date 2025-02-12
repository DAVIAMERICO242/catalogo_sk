package com.skyler.catalogo.domain.shipping;

import com.skyler.catalogo.domain.descontos.interfaces.Discountable;
import com.skyler.catalogo.domain.pedidos.DTOs.LojaPedidoDTO;
import com.skyler.catalogo.domain.pedidos.DTOs.ProdutoPedidoDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShippingCalculationRequest {
    PacSedexEnum pacSedex;//pode ser nulo se for calculado por faixa de cep
    String cep;
    String lojaId;
    List<ProdutoPedidoDTO> produtos = new ArrayList<>();
    public void addProdutoComprado(ProdutoPedidoDTO produto){
        this.produtos.add(produto);
    }

}
