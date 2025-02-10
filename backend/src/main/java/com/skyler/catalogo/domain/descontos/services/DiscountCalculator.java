package com.skyler.catalogo.domain.descontos.services;

import com.skyler.catalogo.domain.descontos.DTOs.AfterAppliedChain;
import com.skyler.catalogo.domain.descontos.DTOs.DescontoAplicadoDTO;
import com.skyler.catalogo.domain.descontos.entities.DelimitedTermos;
import com.skyler.catalogo.domain.descontos.entities.Desconto;
import com.skyler.catalogo.domain.descontos.entities.DescontoProgressivoIntervalos;
import com.skyler.catalogo.domain.descontos.entities.ExcludedTermos;
import com.skyler.catalogo.domain.descontos.enums.DescontoTipo;
import com.skyler.catalogo.domain.descontos.enums.TermoTipo;
import com.skyler.catalogo.domain.descontos.interfaces.Discountable;
import com.skyler.catalogo.domain.descontos.repositories.DescontoRepository;
import com.skyler.catalogo.domain.pedidos.DTOs.ProdutoPedidoDTO;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoRepository;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoVariacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DiscountCalculator {

    private final DescontoRepository descontoRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoVariacaoRepository produtoVariacaoRepository;

    public DiscountCalculator(DescontoRepository descontoRepository, ProdutoRepository produtoRepository, ProdutoVariacaoRepository produtoVariacaoRepository) {
        this.descontoRepository = descontoRepository;
        this.produtoRepository = produtoRepository;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
    }

    public AfterAppliedChain processChainForCurrentEpochAndDiscountable(Discountable discountable){//vale pra carrinho e pedido
        List<DescontoAplicadoDTO> discountChain = new ArrayList<>();
        List<Desconto> descontos = this.descontoRepository.findAllActiveAndNotExpiredByLojaId(
                LocalDate.now(),
                discountable.getLoja().getSystemId()
        );
        List<Produto> produtosBase = this.produtoRepository.findAllByIdIn(discountable.getProdutos().stream().map(o->o.getSystemId()).toList());
        List<ProdutoVariacao> variacoesEntNoDiscountable = new ArrayList<>();
        List<String> variacoesIdOnDiscountable = discountable.getProdutos().stream().flatMap(o->o.getVariacoesCompradas().stream()).map(o->o.getSystemId()).toList();
        Integer cartSize = variacoesIdOnDiscountable.size();
        for(Produto produtoBase:produtosBase){
            for(ProdutoVariacao variacao:produtoBase.getVariacoes()){
                if(variacoesIdOnDiscountable.contains(variacao.getSystemId())){//adicionar o numero de vezes em que se repete a variacao
                    Integer howMany = variacoesIdOnDiscountable.stream().filter(o->o.equals(variacao.getSystemId())).toList().size();
                    for(int i=0;i<howMany;i++){
                        variacoesEntNoDiscountable.add(variacao);
                    }
                }
            }
        }
        Float initialValue = variacoesEntNoDiscountable.stream()
                .map(p -> p.getProduto().getPreco()) // Extrai os preços como Float
                .reduce(0f, Float::sum);
        Float finalValue = initialValue;
        for(Desconto desconto:descontos){
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_FRETE)){
                Float descontoVal = discountable.getValorFrete()*(desconto.getDescontoFrete().getPercentDecimalDiscount());
                finalValue = finalValue - descontoVal;
                DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                descontoAplicadoDTO.setSystemId(desconto.getSystemId());
                descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_FRETE);
                descontoAplicadoDTO.setNome(desconto.getDiscountName());
                descontoAplicadoDTO.setValorAplicado(descontoVal);
                discountChain.add(descontoAplicadoDTO);
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_GENERICO_CARRINHO)){
                if(finalValue>=desconto.getDescontoGenericoCarrinho().getMinValue()){
                    Float descontoVal =  finalValue*(desconto.getDescontoGenericoCarrinho().getPercentDecimalDiscount());
                    finalValue = finalValue - descontoVal;
                    DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                    descontoAplicadoDTO.setSystemId(desconto.getSystemId());
                    descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_GENERICO_CARRINHO);
                    descontoAplicadoDTO.setNome(desconto.getDiscountName());
                    descontoAplicadoDTO.setValorAplicado(descontoVal);
                    discountChain.add(descontoAplicadoDTO);
                }
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_SIMPLES_PRODUTO)){//ERRADO
                for(Produto produto:produtosBase){
                    for(ProdutoVariacao produtoVariacao:produto.getVariacoes()){
                        if(!variacoesIdOnDiscountable.contains(produtoVariacao.getSystemId())){
                            continue;
                        }
                        if(produto.getProdutosCatalogo().contains(desconto.getDescontoSimplesProduto().getProdutoCatalogo())){
                            Float descontoVal = produto.getPreco()*(desconto.getDescontoSimplesProduto().getPercentDecimalDiscount());
                            finalValue = finalValue - descontoVal;
                            DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                            descontoAplicadoDTO.setSystemId(desconto.getSystemId());
                            descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_SIMPLES_PRODUTO);
                            descontoAplicadoDTO.setNome(desconto.getDiscountName());
                            descontoAplicadoDTO.setValorAplicado(descontoVal);
                            discountChain.add(descontoAplicadoDTO);
                        }
                    }
                }
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_SIMPLES_TERMO)){
                for(ProdutoPedidoDTO produto:discountable.getProdutos()){
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
                        if(descontoVal==0f){
                            continue;
                        }
                        finalValue = finalValue - descontoVal;
                        DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                        descontoAplicadoDTO.setSystemId(desconto.getSystemId());
                        descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_SIMPLES_TERMO);
                        descontoAplicadoDTO.setNome(desconto.getDiscountName());
                        descontoAplicadoDTO.setValorAplicado(descontoVal);
                        discountChain.add(descontoAplicadoDTO);
                    }
                }
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_PECA_MAIOR_VALOR)){//nao é cumulativo
                List<Produto> produtosParticipantes = produtosBase;
                if(!desconto.getDelimitedTermos().isEmpty() || !desconto.getExcludedTermos().isEmpty()){
                    produtosParticipantes = this.getProdutosParticipantesDelimitedTermosExcludedTermos(
                            produtosBase,
                            desconto.getDelimitedTermos(),
                            desconto.getExcludedTermos()
                    );
                }
                List<ProdutoVariacao> variacoesDosProdutosParticipantes = produtosParticipantes.stream()
                        .flatMap(o -> o.getVariacoes().stream()) // Transforma a lista em Stream
                        .toList();
                List<ProdutoVariacao> variacoesParticipantes = variacoesEntNoDiscountable.stream().filter(o->variacoesDosProdutosParticipantes.contains(o)).toList();
                if(variacoesParticipantes.size()>=desconto.getDescontoMaiorValor().getLowerQuantityLimitToApply()){
                    Produto produtoComMaiorPreco = produtosParticipantes.stream()
                            .max(Comparator.comparing(Produto::getPreco))
                            .orElse(null); // Retorna null caso o Stream esteja vazio
                    Float descontoVal = produtoComMaiorPreco.getPreco()*(desconto.getDescontoMaiorValor().getPercentDecimalDiscount());
                    finalValue = finalValue - descontoVal;
                    DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                    descontoAplicadoDTO.setSystemId(desconto.getSystemId());
                    descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_PECA_MAIOR_VALOR);
                    descontoAplicadoDTO.setNome(desconto.getDiscountName());
                    descontoAplicadoDTO.setValorAplicado(descontoVal);
                    discountChain.add(descontoAplicadoDTO);
                }
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_PECA_MENOR_VALOR)){//nao é cumulativo
                List<Produto> produtosParticipantes = produtosBase;
                if(!desconto.getDelimitedTermos().isEmpty() || !desconto.getExcludedTermos().isEmpty()){
                    produtosParticipantes = this.getProdutosParticipantesDelimitedTermosExcludedTermos(
                            produtosBase,
                            desconto.getDelimitedTermos(),
                            desconto.getExcludedTermos()
                    );
                }
                List<ProdutoVariacao> variacoesDosProdutosParticipantes = produtosParticipantes.stream()
                        .flatMap(o -> o.getVariacoes().stream()) // Transforma a lista em Stream
                        .toList();
                List<ProdutoVariacao> variacoesParticipantes = variacoesEntNoDiscountable.stream().filter(o->variacoesDosProdutosParticipantes.contains(o)).toList();
                if(variacoesParticipantes.size()>=desconto.getDescontoMenorValor().getLowerQuantityLimitToApply()){
                    Produto produtoComMenorPreco = produtosParticipantes.stream()
                            .min(Comparator.comparing(Produto::getPreco))
                            .orElse(null); // Retorna null caso o Stream esteja vazio
                    Float descontoVal = produtoComMenorPreco.getPreco()*(desconto.getDescontoMenorValor().getPercentDecimalDiscount());
                    finalValue = finalValue - descontoVal;
                    DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                    descontoAplicadoDTO.setSystemId(desconto.getSystemId());
                    descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_PECA_MENOR_VALOR);
                    descontoAplicadoDTO.setNome(desconto.getDiscountName());
                    descontoAplicadoDTO.setValorAplicado(descontoVal);
                    discountChain.add(descontoAplicadoDTO);
                }

            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_PROGRESSIVO)){
                List<Produto> produtosParticipantes = produtosBase;
                if(!desconto.getDelimitedTermos().isEmpty() || !desconto.getExcludedTermos().isEmpty()){
                    produtosParticipantes = this.getProdutosParticipantesDelimitedTermosExcludedTermos(
                            produtosBase,
                            desconto.getDelimitedTermos(),
                            desconto.getExcludedTermos()
                    );
                }
                List<ProdutoVariacao> variacoesDosProdutosParticipantes = produtosParticipantes.stream()
                        .flatMap(o -> o.getVariacoes().stream()) // Transforma a lista em Stream
                        .toList();
                List<ProdutoVariacao> variacoesParticipantes = variacoesEntNoDiscountable.stream().filter(o->variacoesDosProdutosParticipantes.contains(o)).toList();
                Set<DescontoProgressivoIntervalos> descontoProgressivoIntervalos = desconto.getDescontoProgressivo().getIntervalos();
                List<DescontoProgressivoIntervalos> intervalosOrdenadosDesc = descontoProgressivoIntervalos.stream()
                        .sorted(Comparator.comparing(DescontoProgressivoIntervalos::getMinQuantity).reversed())
                        .toList();
                Float precoVariacoesParticipantes = variacoesParticipantes.stream()
                        .map(p -> p.getProduto().getPreco()) // Extrai os preços como Float
                        .reduce(0f, Float::sum);
                for(DescontoProgressivoIntervalos intervalo:intervalosOrdenadosDesc){
                    if(variacoesParticipantes.size()>=intervalo.getMinQuantity()){
                        Float descontoVal = precoVariacoesParticipantes*intervalo.getPercentDecimalDiscount();
                        finalValue = finalValue - descontoVal;
                        DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                        descontoAplicadoDTO.setSystemId(desconto.getSystemId());
                        descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_PROGRESSIVO);
                        descontoAplicadoDTO.setNome(desconto.getDiscountName());
                        descontoAplicadoDTO.setValorAplicado(descontoVal);
                        discountChain.add(descontoAplicadoDTO);
                        break;

                    }
                }
            }
        }
        return new AfterAppliedChain(discountChain,finalValue);
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
            if(delimitedCategorias.isEmpty() && delimitedLinhas.isEmpty() && delimitedGrupos.isEmpty()){//passou no filtro de exclusao e nao tem filtro limitando
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
            return produto.getPreco()*(knownDescontoPercentage);
        }
        if(delimitedLinhas.contains(produto.getLinha())){
            return produto.getPreco()*(knownDescontoPercentage);
        }
        if(delimitedGrupos.contains(produto.getGrupo())){
            return produto.getPreco()*(knownDescontoPercentage);
        }
        if(delimitedCategorias.isEmpty() && delimitedLinhas.isEmpty() && delimitedGrupos.isEmpty()){//passou na exclusao e nao tem termo limitando
            return produto.getPreco()*(knownDescontoPercentage);
        }
        return 0f;
    }



}
