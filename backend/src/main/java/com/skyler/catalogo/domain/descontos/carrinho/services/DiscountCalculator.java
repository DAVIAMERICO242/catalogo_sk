package com.skyler.catalogo.domain.descontos.carrinho.services;

import com.skyler.catalogo.domain.descontos.carrinho.DTOs.DescontoAplicadoDTO;
import com.skyler.catalogo.domain.descontos.carrinho.entities.DelimitedTermos;
import com.skyler.catalogo.domain.descontos.carrinho.entities.Desconto;
import com.skyler.catalogo.domain.descontos.carrinho.entities.DescontoProgressivoIntervalos;
import com.skyler.catalogo.domain.descontos.carrinho.entities.ExcludedTermos;
import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.enums.TermoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.interfaces.Discountable;
import com.skyler.catalogo.domain.descontos.carrinho.repositories.DescontoRepository;
import com.skyler.catalogo.domain.pedidos.PedidoDTO;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoRepository;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoVariacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<DescontoAplicadoDTO> getDiscountChainForCurrentEpochAndDiscountable(Discountable discountable){//vale pra carrinho e pedido
        List<DescontoAplicadoDTO> output = new ArrayList<>();
        List<Desconto> descontos = this.descontoRepository.findAllActiveAndNotExpiredByLojaId(
                LocalDate.now(),
                discountable.getLoja().getSystemId()
        );
        List<Produto> produtosBase = this.produtoRepository.findAllByIdIn(discountable.getProdutos().stream().map(o->o.getSystemId()).toList());
        Float initialValue = produtosBase.stream()
                .map(p -> p.getPreco()) // Extrai os preços como Float
                .reduce(0f, Float::sum);
        Float finalValue = initialValue;
        for(Desconto desconto:descontos){
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_FRETE)){
                Float descontoVal = discountable.getValorFrete()*(1 - desconto.getDescontoFrete().getPercentDecimalDiscount());
                finalValue = finalValue - descontoVal;
                DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_FRETE);
                descontoAplicadoDTO.setNome(desconto.getDiscountName());
                descontoAplicadoDTO.setValorAplicado(descontoVal);
                output.add(descontoAplicadoDTO);
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_GENERICO_CARRINHO)){
                Float descontoVal =  finalValue*(desconto.getDescontoGenericoCarrinho().getPercentDecimalDiscount());
                finalValue = finalValue - descontoVal;
                DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_GENERICO_CARRINHO);
                descontoAplicadoDTO.setNome(desconto.getDiscountName());
                descontoAplicadoDTO.setValorAplicado(descontoVal);
                output.add(descontoAplicadoDTO);
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_SIMPLES_PRODUTO)){//errado, tem que loopar nas variações
                for(Produto produto:produtosBase){
                    if(produto.getProdutosCatalogo().contains(desconto.getDescontoSimplesProduto().getProdutoCatalogo())){
                        Float descontoVal = produto.getPreco()*(1 - desconto.getDescontoSimplesProduto().getPercentDecimalDiscount());
                        finalValue = finalValue - descontoVal;
                        DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                        descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_SIMPLES_PRODUTO);
                        descontoAplicadoDTO.setNome(desconto.getDiscountName());
                        descontoAplicadoDTO.setValorAplicado(descontoVal);
                        output.add(descontoAplicadoDTO);
                    }
                }
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_SIMPLES_TERMO)){
                for(Discountable.Produto produto:discountable.getProdutos()){
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
                        DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                        descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_SIMPLES_TERMO);
                        descontoAplicadoDTO.setNome(desconto.getDiscountName());
                        descontoAplicadoDTO.setValorAplicado(descontoVal);
                        output.add(descontoAplicadoDTO);
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
                    Float descontoVal = produtoComMaiorPreco.getPreco()*(1-desconto.getDescontoMaiorValor().getPercentDecimalDiscount());
                    finalValue = finalValue - descontoVal;
                    DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                    descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_PECA_MAIOR_VALOR);
                    descontoAplicadoDTO.setNome(desconto.getDiscountName());
                    descontoAplicadoDTO.setValorAplicado(descontoVal);
                    output.add(descontoAplicadoDTO);
                }
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_PECA_MENOR_VALOR)){//nao é cumulativo
                List<Produto> produtosParticipantes = this.getProdutosParticipantesDelimitedTermosExcludedTermos(
                        produtosBase,
                        desconto.getDelimitedTermos(),
                        desconto.getExcludedTermos()
                );
                if(!produtosParticipantes.isEmpty()){
                    Produto produtoComMenorPreco = produtosParticipantes.stream()
                            .min(Comparator.comparing(Produto::getPreco))
                            .orElse(null); // Retorna null caso o Stream esteja vazio
                    Float descontoVal = produtoComMenorPreco.getPreco()*(1-desconto.getDescontoMenorValor().getPercentDecimalDiscount());
                    finalValue = finalValue - descontoVal;
                    DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                    descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_PECA_MENOR_VALOR);
                    descontoAplicadoDTO.setNome(desconto.getDiscountName());
                    descontoAplicadoDTO.setValorAplicado(descontoVal);
                    output.add(descontoAplicadoDTO);
                }
            }
            if(desconto.getDescontoTipo().equals(DescontoTipo.DESCONTO_PROGRESSIVO)){
                List<Produto> produtosParticipantes = this.getProdutosParticipantesDelimitedTermosExcludedTermos(
                        produtosBase,
                        desconto.getDelimitedTermos(),
                        desconto.getExcludedTermos()
                );
                if(!produtosParticipantes.isEmpty()){
                    List<String> produtosParticipantesIds = produtosParticipantes.stream().map(o->o.getSystemId()).toList();
                    List<Discountable.ProdutoVariacao> variacoesParticipantesFromDiscountableNotUnique = discountable.getProdutos().stream()
                            .filter(o->produtosParticipantesIds.contains(o.getSystemId()))
                            .flatMap(produto -> produto.getVariacoesCompradas().stream())
                            .toList();
                    Set<DescontoProgressivoIntervalos> descontoProgressivoIntervalos = desconto.getDescontoProgressivo().getIntervalos();
                    List<DescontoProgressivoIntervalos> intervalosOrdenadosDesc = descontoProgressivoIntervalos.stream()
                            .sorted(Comparator.comparing(DescontoProgressivoIntervalos::getMinQuantity).reversed())
                            .toList();
                    for(DescontoProgressivoIntervalos intervalo:intervalosOrdenadosDesc){
                        if(variacoesParticipantesFromDiscountableNotUnique.size()>=intervalo.getMinQuantity()){
                            Float descontoVal = finalValue*intervalo.getPercentDecimalDiscount();
                            finalValue = finalValue - descontoVal;
                            DescontoAplicadoDTO descontoAplicadoDTO = new DescontoAplicadoDTO();
                            descontoAplicadoDTO.setTipo(DescontoTipo.DESCONTO_PROGRESSIVO);
                            descontoAplicadoDTO.setNome(desconto.getDiscountName());
                            descontoAplicadoDTO.setValorAplicado(descontoVal);
                            output.add(descontoAplicadoDTO);
                            break;
                        }
                    }
                }
            }
        }
        return output;
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
