package com.skyler.catalogo.domain.pedidos;

import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PedidoDTO {
    private String systemId;
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
    private List<ProdutoVariacao> variacoesCompradas = new ArrayList<>();
    private List<Desconto> descontosAplicados = new ArrayList<>();

    public void addVariacaoComprada(ProdutoVariacao variacao){
        this.variacoesCompradas.add(variacao);
    }

    public void addDescontoAplicado(Desconto desconto){
        this.descontosAplicados.add(desconto);
    }

    @Data
    public static class ProdutoVariacao{
        String systemId;
        String nomeProdutoBase;
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
