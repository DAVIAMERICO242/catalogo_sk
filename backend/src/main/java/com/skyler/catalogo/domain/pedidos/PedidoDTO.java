package com.skyler.catalogo.domain.pedidos;

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
    private Loja loja;
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
    private List<Produto> produtos = new ArrayList<>();
    private List<Desconto> descontosAplicados = new ArrayList<>();

    public void addProdutoComprado(Produto produto){
        this.produtos.add(produto);
    }

    public void addDescontoAplicado(Desconto desconto){
        this.descontosAplicados.add(desconto);
    }

    @Data
    public static class Loja{
        String systemId;
        String slug;
        String nome;
    }

    @Data
    public static class Produto{
        String systemId;
        String sku;
        String nome;
        Float valorBase;
        List<ProdutoVariacao> variacoesCompradas = new ArrayList<>();
        public void addVariacaoComprada(ProdutoVariacao variacaoComprada){
            this.variacoesCompradas.add(variacaoComprada);
        }
    }

    @Data
    public static class ProdutoVariacao{
        String systemId;
        String sku;
        String cor;
        String tamanho;
        Float valorBase;
        String fotoUrl;
    }

    @Data
    public static class Desconto{
        String systemId;
        String nome;
        DescontoTipo tipo;
        Float valorAplicado;
    }
}
