package com.skyler.catalogo.domain.pedidos.DTOs;
import com.skyler.catalogo.domain.descontos.interfaces.Discountable;
import com.skyler.catalogo.domain.pedidos.BasicOrder;
import com.skyler.catalogo.domain.pedidos.FreteEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PedidoBeforeCalculationsDTO implements Discountable,BasicOrder {
    private LojaPedidoDTO loja;
    private String documento;
    private String nome;
    private Boolean entregaLoja;
    private FreteEnum tipoFrete;//so existe se entrega loja nao for true
    private Integer numero;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefone;
    private Float valorFrete;
    private List<ProdutoPedidoDTO> produtos = new ArrayList<>();
    public void addProdutoComprado(ProdutoPedidoDTO produto){
        this.produtos.add(produto);
    }
}
