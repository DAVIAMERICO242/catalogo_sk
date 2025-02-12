package com.skyler.catalogo.domain.pedidos.DTOs;

import com.skyler.catalogo.domain.descontos.DTOs.DescontoAplicadoDTO;
import com.skyler.catalogo.domain.pedidos.BasicOrder;
import com.skyler.catalogo.domain.pedidos.FreteEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PedidoAfterCalculationsDTO implements BasicOrder {
    private String systemId;
    private LojaPedidoDTO loja;
    private LocalDateTime moment;
    private Boolean entregaLoja;
    private FreteEnum tipoFrete;//so existe se entrega loja nao for true
    private String documento;
    private String nome;
    private Integer numero;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefone;
    private Float valorFrete;
    private Boolean pago;
    private Float valor;
    private List<ProdutoPedidoDTO> produtos = new ArrayList<>();
    private List<DescontoAplicadoDTO> descontosAplicados = new ArrayList<>();


    public void addProdutoComprado(ProdutoPedidoDTO produto){
        this.produtos.add(produto);
    }

    public void addDescontoAplicado(DescontoAplicadoDTO desconto){
        this.descontosAplicados.add(desconto);
    }


}
