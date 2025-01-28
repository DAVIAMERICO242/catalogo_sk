package com.skyler.catalogo.domain.pedidos;

import com.skyler.catalogo.domain.descontos.carrinho.DTOs.DescontoAplicadoDTO;
import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.interfaces.Discountable;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PedidoDTO implements Discountable {
    private String systemId;
    private Discountable.Loja loja;
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
    private Float valor;
    private Float valorFrete;
    private Boolean pago;
    private List<Discountable.Produto> produtos = new ArrayList<>();
    private List<DescontoAplicadoDTO> descontosAplicados = new ArrayList<>();

    @Override
    public Discountable.Loja getLoja(){
        return this.loja;
    }

    @Override
    public List<Discountable.Produto> getProdutos(){
        return this.produtos;
    }


    public void addProdutoComprado(Produto produto){
        this.produtos.add(produto);
    }

    public void addDescontoAplicado(DescontoAplicadoDTO desconto){
        this.descontosAplicados.add(desconto);
    }

    @Data
    public static class Loja implements Discountable.Loja{
        String systemId;
        String slug;
        String nome;
    }

    @Data
    public static class Produto implements Discountable.Produto{
        String systemId;
        String sku;
        String nome;
        Float valorBase;
        List<Discountable.ProdutoVariacao> variacoesCompradas = new ArrayList<>();
        public void addVariacaoComprada(Discountable.ProdutoVariacao variacaoComprada){
            this.variacoesCompradas.add(variacaoComprada);
        }
    }

    @Data
    public static class ProdutoVariacao implements Discountable.ProdutoVariacao{
        String systemId;
        String sku;
        String cor;
        String tamanho;
        Float valorBase;
        String fotoUrl;
    }

}
