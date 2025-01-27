package com.skyler.catalogo.domain.descontos.carrinho.services;

import com.skyler.catalogo.domain.descontos.carrinho.DTOs.*;
import com.skyler.catalogo.domain.descontos.carrinho.entities.*;
import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.enums.TermoTipo;
import org.springframework.stereotype.Service;

@Service
public class DescontoMapper {

    public DescontoDTO entityToDTO(Desconto entity){
        if(entity.getDescontoTipo().equals(DescontoTipo.DESCONTO_FRETE)){
            return this.descontoFrete(entity);
        }
        if(entity.getDescontoTipo().equals(DescontoTipo.DESCONTO_GENERICO_CARRINHO)){
            return this.genericoCarrinho(entity);
        }
        if(entity.getDescontoTipo().equals(DescontoTipo.DESCONTO_SIMPLES_PRODUTO)){
            return this.descontoSimplesProduto(entity);
        }
        if(entity.getDescontoTipo().equals(DescontoTipo.DESCONTO_SIMPLES_TERMO)){
            return this.descontoSimplesTermo(entity);
        }
        if(entity.getDescontoTipo().equals(DescontoTipo.DESCONTO_PECA_MAIOR_VALOR)){
            return this.descontoPecaMaiorValor(entity);
        }
        if(entity.getDescontoTipo().equals(DescontoTipo.DESCONTO_PECA_MENOR_VALOR)){
            return this.descontoPecaMenorValor(entity);
        }
        if(entity.getDescontoTipo().equals(DescontoTipo.DESCONTO_PROGRESSIVO)){
            return this.descontoProgressivo(entity);
        }
        return null;
    }

    private DescontoDTO descontoProgressivo(Desconto entity){
        DescontoDTO output = this.arrumarEssencial(entity);
        DescontoProgressivo descontoProgressivo = entity.getDescontoProgressivo();
        DescontoProgressivoDTO descontoProgressivoDTO = new DescontoProgressivoDTO();
        descontoProgressivoDTO.setSystemId(descontoProgressivo.getSystemId());
        for(DescontoProgressivoIntervalos descontoProgressivoIntervalo:entity.getDescontoProgressivo().getIntervalos()){
            IntervalDescontoDTO intervalDescontoDTO = new IntervalDescontoDTO();
            intervalDescontoDTO.setPercentDecimalDiscount(descontoProgressivoIntervalo.getPercentDecimalDiscount());
            intervalDescontoDTO.setMinQuantity(descontoProgressivoIntervalo.getMinQuantity());
            descontoProgressivoDTO.addIntervalo(intervalDescontoDTO);
        }
        this.arrumarTermos(entity,descontoProgressivoDTO);
        output.setDescontoProgressivo(descontoProgressivoDTO);
        return output;
    }

    private DescontoDTO descontoPecaMenorValor(Desconto entity){
        DescontoDTO output = this.arrumarEssencial(entity);
        DescontoMenorValor descontoMenorValor = entity.getDescontoMenorValor();
        DescontoMenorValorDTO descontoMenorValorDTO = new DescontoMenorValorDTO();
        descontoMenorValorDTO.setSystemId(descontoMenorValor.getSystemId());
        descontoMenorValorDTO.setLowerQuantityLimitToApply(descontoMenorValor.getLowerQuantityLimitToApply());
        descontoMenorValorDTO.setPercentDecimalDiscount(descontoMenorValor.getPercentDecimalDiscount());
        this.arrumarTermos(entity,descontoMenorValorDTO);
        output.setDescontoMenorValor(descontoMenorValorDTO);
        return output;
    }

    private DescontoDTO descontoPecaMaiorValor(Desconto entity){
        DescontoDTO output = this.arrumarEssencial(entity);
        DescontoMaiorValor descontoMaiorValor = entity.getDescontoMaiorValor();
        DescontoMaiorValorDTO descontoMaiorValorDTO = new DescontoMaiorValorDTO();
        descontoMaiorValorDTO.setSystemId(descontoMaiorValor.getSystemId());
        descontoMaiorValorDTO.setLowerQuantityLimitToApply(descontoMaiorValor.getLowerQuantityLimitToApply());
        descontoMaiorValorDTO.setPercentDecimalDiscount(descontoMaiorValor.getPercentDecimalDiscount());
        this.arrumarTermos(entity,descontoMaiorValorDTO);
        output.setDescontoMaiorValor(descontoMaiorValorDTO);
        return output;
    }

    private DescontoDTO descontoSimplesTermo(Desconto entity){
        DescontoDTO output = this.arrumarEssencial(entity);
        DescontoSimplesTermo descontoSimplesTermo = entity.getDescontoSimplesTermo();
        DescontoSimplesTermoDTO descontoSimplesTermoDTO = new DescontoSimplesTermoDTO();
        descontoSimplesTermoDTO.setSystemId(descontoSimplesTermo.getSystemId());
        descontoSimplesTermoDTO.setPercentDecimalDiscount(descontoSimplesTermo.getPercentDecimalDiscount());
        this.arrumarTermos(entity,descontoSimplesTermoDTO);
        output.setDescontoSimplesTermo(descontoSimplesTermoDTO);
        return output;
    }

    private DescontoDTO descontoSimplesProduto(Desconto entity){
        DescontoDTO output = this.arrumarEssencial(entity);
        DescontoSimplesProduto descontoSimplesProduto = entity.getDescontoSimplesProduto();
        DescontoSimplesDTO descontoSimplesDTO = new DescontoSimplesDTO();
        descontoSimplesDTO.setSystemId(descontoSimplesProduto.getSystemId());
        descontoSimplesDTO.setPercentDecimalDiscount(descontoSimplesProduto.getPercentDecimalDiscount());
        ProdutoDescontoDTO produtoDescontoDTO = new ProdutoDescontoDTO();
        produtoDescontoDTO.setSystemId(descontoSimplesProduto.getProdutoCatalogo().getSystemId());
        produtoDescontoDTO.setNome(descontoSimplesProduto.getProdutoCatalogo().getProdutoBaseFranquia().getDescricao());
        produtoDescontoDTO.setBaseValue(descontoSimplesProduto.getProdutoCatalogo().getProdutoBaseFranquia().getPreco());
        produtoDescontoDTO.setCatalogValue(descontoSimplesProduto.getProdutoCatalogo().getCatalogPrice());
        descontoSimplesDTO.setProduto(produtoDescontoDTO);
        output.setDescontoSimples(descontoSimplesDTO);
        return output;
    }

    private DescontoDTO genericoCarrinho(Desconto entity){
        DescontoDTO output = this.arrumarEssencial(entity);
        DescontoGenericoCarrinho descontoGenericoCarrinho = entity.getDescontoGenericoCarrinho();
        DescontoGenericoCarrinhoDTO descontoGenericoCarrinhoDTO = new DescontoGenericoCarrinhoDTO();
        descontoGenericoCarrinhoDTO.setSystemId(descontoGenericoCarrinho.getSystemId());
        descontoGenericoCarrinhoDTO.setPercentDecimalDiscount(descontoGenericoCarrinho.getPercentDecimalDiscount());
        descontoGenericoCarrinhoDTO.setMinValue(descontoGenericoCarrinho.getMinValue());
        output.setDescontoGenericoCarrinho(descontoGenericoCarrinhoDTO);
        return output;
    }

    private DescontoDTO descontoFrete(Desconto entity){
        DescontoDTO output = this.arrumarEssencial(entity);
        DescontoFrete descontoFrete = entity.getDescontoFrete();
        DescontoFreteDTO descontoFreteDTO = new DescontoFreteDTO();
        descontoFreteDTO.setSystemId(descontoFrete.getSystemId());
        descontoFreteDTO.setPercentDecimalDiscount(descontoFrete.getPercentDecimalDiscount());
        descontoFreteDTO.setLowerValueLimitToApply(descontoFrete.getLowerValueLimitToApply());
        output.setDescontoFrete(descontoFreteDTO);
        return output;
    }

    private void arrumarTermos(Desconto entity, DelimitedExcludedInterface object){
        for(DelimitedTermos delimitedTermo:entity.getDelimitedTermos()){
            if(delimitedTermo.getClassificacaoTermo().equals(TermoTipo.CATEGORIA)){
                object.addDelimitedCategoria(delimitedTermo.getTermo());
            }
            if(delimitedTermo.getClassificacaoTermo().equals(TermoTipo.LINHA)){
                object.addDelimitedLinha(delimitedTermo.getTermo());
            }
            if(delimitedTermo.getClassificacaoTermo().equals(TermoTipo.GRUPO)){
                object.addDelimitedGrupo(delimitedTermo.getTermo());
            }
        }
        for(ExcludedTermos excludedTermo:entity.getExcludedTermos()){
            if(excludedTermo.getClassificacaoTermo().equals(TermoTipo.CATEGORIA)){
                object.addExcudedCategoria(excludedTermo.getTermo());
            }
            if(excludedTermo.getClassificacaoTermo().equals(TermoTipo.LINHA)){
                object.addExcludedLinha(excludedTermo.getTermo());
            }
            if(excludedTermo.getClassificacaoTermo().equals(TermoTipo.GRUPO)){
                object.addExcludedGrupo(excludedTermo.getTermo());
            }
        }
    }

    private DescontoDTO arrumarEssencial(Desconto entity){
        DescontoDTO output = new DescontoDTO();
        LojaDescontoDTO lojaDescontoDTO = new LojaDescontoDTO();
        output.setSystemId(entity.getSystemId());
        output.setNome(entity.getDiscountName());
        output.setIsActive(entity.getIsActive());
        output.setExpiresAt(entity.getExpiresAt());
        output.setTipo(entity.getDescontoTipo());
        output.setCreatedAt(entity.getCreatedAt());
        lojaDescontoDTO.setSystemId(entity.getLoja().getSystemId());
        lojaDescontoDTO.setNome(entity.getLoja().getNome());
        lojaDescontoDTO.setSlug(entity.getLoja().getSlug());
        output.setLoja(lojaDescontoDTO);
        return output;
    }

}
