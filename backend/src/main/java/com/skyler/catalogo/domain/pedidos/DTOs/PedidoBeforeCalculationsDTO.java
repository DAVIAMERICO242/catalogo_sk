package com.skyler.catalogo.domain.pedidos.DTOs;

import com.skyler.catalogo.domain.descontos.DTOs.descontavel.LojaDescontavel;
import com.skyler.catalogo.domain.descontos.DTOs.descontavel.ProdutoDescontavel;
import com.skyler.catalogo.domain.descontos.interfaces.Discountable;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PedidoBeforeCalculationsDTO implements Discountable {
    private LojaDescontavel loja;
    private LocalDateTime moment;
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
    private List<ProdutoDescontavel> produtos = new ArrayList<>();
    public void addProdutoComprado(ProdutoDescontavel produto){
        this.produtos.add(produto);
    }

}
