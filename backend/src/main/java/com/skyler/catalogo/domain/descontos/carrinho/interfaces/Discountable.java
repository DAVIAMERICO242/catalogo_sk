package com.skyler.catalogo.domain.descontos.carrinho.interfaces;

import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;

import java.util.List;

public interface Discountable {

    Float getValorFrete();
    List<Produto> getProdutos();
    Loja getLoja();

    interface Loja {
        String getSystemId();
        String getSlug();
        String getNome();
    }

    interface Produto {
        String getSystemId();
        String getSku();
        String getNome();
        Float getValorBase();
        List<ProdutoVariacao> getVariacoesCompradas();
    }

    interface ProdutoVariacao {
        String getSystemId();
        String getSku();
        String getCor();
        String getTamanho();
        Float getValorBase();
        String getFotoUrl();
    }
}
