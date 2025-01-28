package com.skyler.catalogo.domain.descontos.carrinho.services;

import com.skyler.catalogo.domain.descontos.carrinho.entities.DelimitedTermos;
import com.skyler.catalogo.domain.descontos.carrinho.entities.Desconto;
import com.skyler.catalogo.domain.descontos.carrinho.entities.ExcludedTermos;
import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.enums.TermoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.repositories.DescontoRepository;
import com.skyler.catalogo.domain.pedidos.PedidoDTO;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoRepository;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoVariacaoRepository;

import java.time.LocalDate;
import java.util.*;

public class DiscountCalculator {

    private final DescontoRepository descontoRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoVariacaoRepository produtoVariacaoRepository;

    public DiscountCalculator(DescontoRepository descontoRepository, ProdutoRepository produtoRepository, ProdutoVariacaoRepository produtoVariacaoRepository) {
        this.descontoRepository = descontoRepository;
        this.produtoRepository = produtoRepository;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
    }

    private Float getFinalDiscountValueForCurrentEpochAndOrder(PedidoDTO pedidoDTO){
        List<Desconto> descontos = this.descontoRepository.findAllActiveAndNotExpiredByLojaId(
                LocalDate.now(),
                pedidoDTO.getLoja().getSystemId()
        );
        List<Produto> produtosBase = this.produtoRepository.findAllByIdIn(pedidoDTO.getProdutosComprados().stream().map(o->o.getSystemId()).toList());
        Float initialValue = produtosBase.stream()
                .map(p -> p.getPreco()) // Extrai os preços como Float
                .reduce(0f, Float::sum);
        Float finalValue = initialValue;
        for(Desconto desconto:descontos){
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_FRETE)){
                finalValue = finalValue - pedidoDTO.getValorFrete()*(1 - desconto.getDescontoFrete().getPercentDecimalDiscount());
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_GENERICO_CARRINHO)){
                finalValue = finalValue - finalValue*(desconto.getDescontoGenericoCarrinho().getPercentDecimalDiscount());
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_SIMPLES_PRODUTO)){//errado, tem que loopar nas variações
                for(Produto produto:produtosBase){
                    if(produto.getProdutosCatalogo().contains(desconto.getDescontoSimplesProduto().getProdutoCatalogo())){
                        finalValue = finalValue - produto.getPreco()*(1 - desconto.getDescontoSimplesProduto().getPercentDecimalDiscount());
                    }
                }
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_SIMPLES_TERMO)){
                for(PedidoDTO.Produto produto:pedidoDTO.getProdutosComprados()){
                    Produto regardingProduto = produtosBase.stream().filter(o->o.getSystemId().equals(produto.getSystemId())).findFirst().orElse(null);
                    if(regardingProduto==null){
                        continue;
                    }
                    for(int i = 0;i<produto.getVariacoesCompradas().size();i++){
                        Float descontoVal = this.discountSimplesTermo(
                                regardingProduto,
                                desconto.getDelimitedTermos(),
                                desconto.getExcludedTermos(),
                                desconto.getDescontoSimplesTermo().getPercentDecimalDiscount()
                        );
                        finalValue = finalValue - descontoVal;
                    }
                }
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_PECA_MAIOR_VALOR)){//nao é cumulativo
                List<Produto> produtosParticipantes = this.getProdutosParticipantesDelimitedTermosExcludedTermos(
                        produtosBase,
                        desconto.getDelimitedTermos(),
                        desconto.getExcludedTermos()
                );
                if(!produtosParticipantes.isEmpty()){
                    Produto produtoComMaiorPreco = produtosParticipantes.stream()
                            .max(Comparator.comparing(Produto::getPreco))
                            .orElse(null); // Retorna null caso o Stream esteja vazio
                    finalValue = finalValue - produtoComMaiorPreco.getPreco()*(1-desconto.getDescontoMaiorValor().getPercentDecimalDiscount());
                }
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_PECA_MENOR_VALOR)){//nao é cumulativo
                List<Produto> produtosParticipantes = this.getProdutosParticipantesDelimitedTermosExcludedTermos(
                        produtosBase,
                        desconto.getDelimitedTermos(),
                        desconto.getExcludedTermos()
                );
                if(!produtosParticipantes.isEmpty()){
                    Produto produtoComMaiorPreco = produtosParticipantes.stream()
                            .min(Comparator.comparing(Produto::getPreco))
                            .orElse(null); // Retorna null caso o Stream esteja vazio
                    finalValue = finalValue - produtoComMaiorPreco.getPreco()*(1-desconto.getDescontoMaiorValor().getPercentDecimalDiscount());
                }
            }
        }
        return finalValue;
    }


    private List<Produto> getProdutosParticipantesDelimitedTermosExcludedTermos(
            List<Produto> produtosBase,
            Set<DelimitedTermos> delimitedTermos,
            Set<ExcludedTermos> excludedTermos
    ){
        List<String> excludedCategorias = excludedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.CATEGORIA)).map(o->o.getTermo()).toList();
        List<String> excludedLinhas = excludedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.LINHA)).map(o->o.getTermo()).toList();
        List<String> excludedGrupos = excludedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.GRUPO)).map(o->o.getTermo()).toList();
        List<String> delimitedCategorias = delimitedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.CATEGORIA)).map(o->o.getTermo()).toList();
        List<String> delimitedLinhas = delimitedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.LINHA)).map(o->o.getTermo()).toList();
        List<String> delimitedGrupos = delimitedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.GRUPO)).map(o->o.getTermo()).toList();
        return produtosBase.stream().filter(o->{
            if(excludedCategorias.contains(o.getCategoria())){
                return false;
            }
            if(excludedLinhas.contains(o.getLinha())){
                return false;
            }
            if(excludedGrupos.contains(o.getGrupo())){
                return false;
            }
            if(delimitedCategorias.contains(o.getCategoria())){
                return true;
            }
            if(delimitedLinhas.contains(o.getLinha())){
                return true;
            }
            if(delimitedGrupos.contains(o.getGrupo())){
                return true;
            }
            return false;
        }).toList();
    }


    private Float discountSimplesTermo(
            Produto produto,
            Set<DelimitedTermos> delimitedTermos,
            Set<ExcludedTermos> excludedTermos,
            Float knownDescontoPercentage
    ){
        List<String> excludedCategorias = excludedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.CATEGORIA)).map(o->o.getTermo()).toList();
        List<String> excludedLinhas = excludedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.LINHA)).map(o->o.getTermo()).toList();
        List<String> excludedGrupos = excludedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.GRUPO)).map(o->o.getTermo()).toList();
        if(excludedCategorias.contains(produto.getCategoria())){
            return 0f;
        }
        if(excludedLinhas.contains(produto.getLinha())){
            return 0f;
        }
        if(excludedGrupos.contains(produto.getGrupo())){
            return 0f;
        }
        List<String> delimitedCategorias = delimitedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.CATEGORIA)).map(o->o.getTermo()).toList();
        List<String> delimitedLinhas = delimitedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.LINHA)).map(o->o.getTermo()).toList();
        List<String> delimitedGrupos = delimitedTermos.stream()
                .filter(o->o.getClassificacaoTermo().equals(TermoTipo.GRUPO)).map(o->o.getTermo()).toList();
        if(delimitedCategorias.contains(produto.getCategoria())){
            return produto.getPreco()*(1 - knownDescontoPercentage);
        }
        if(delimitedLinhas.contains(produto.getLinha())){
            return produto.getPreco()*(1 - knownDescontoPercentage);
        }
        if(delimitedGrupos.contains(produto.getGrupo())){
            return produto.getPreco()*(1 - knownDescontoPercentage);
        }
        return 0f;
    }



}
