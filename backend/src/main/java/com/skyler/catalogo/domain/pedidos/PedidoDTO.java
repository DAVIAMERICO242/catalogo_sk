package com.skyler.catalogo.domain.pedidos;

import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import lombok.Data;

import java.time.LocalDateTime;
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
    private List<ProdutoVariacao> variacoesCompradas;
    private List<Desconto> descontosAplicados;

    @Data
    public static class ProdutoVariacao{
        String systemId;
        String nome;
        String sku;
        Float valorBase;
    }

    @Data
    public static class Desconto{
        String systemId;
        String nome;
        DescontoTipo tipo;
        Float valorAplicado;
    }
}
