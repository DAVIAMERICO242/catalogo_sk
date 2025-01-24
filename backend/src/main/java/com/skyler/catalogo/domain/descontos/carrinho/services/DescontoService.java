package com.skyler.catalogo.domain.descontos.carrinho.services;

import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogoRepository;
import com.skyler.catalogo.domain.descontos.carrinho.DTOs.*;
import com.skyler.catalogo.domain.descontos.carrinho.entities.*;
import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.enums.TermoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.repositories.DescontoRepository;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DescontoService {

    private final LojaRepository lojaRepository;
    private final ProdutoCatalogoRepository produtoCatalogoRepository;
    private final DescontoRepository descontoRepository;
    private final DescontoMapper descontoMapper;

    public DescontoService(LojaRepository lojaRepository, ProdutoCatalogoRepository produtoCatalogoRepository, DescontoRepository descontoRepository, DescontoMapper descontoMapper) {
        this.lojaRepository = lojaRepository;
        this.produtoCatalogoRepository = produtoCatalogoRepository;
        this.descontoRepository = descontoRepository;
        this.descontoMapper = descontoMapper;
    }

    public List<DescontoDTO> getDescontosForLoja(String lojaSystemId){
        List<DescontoDTO> descontoDTOList = new ArrayList<>();
        List<Desconto> descontos = this.descontoRepository.findAllByLojaId(lojaSystemId);
        for(Desconto desconto:descontos){
            descontoDTOList.add(this.descontoMapper.entityToDTO(desconto));
        }
        return descontoDTOList;
    }

    public DescontoDTO cadastrarAtualizarDesconto(DescontoDTO descontoDTO){
        if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_FRETE)){
            descontoDTO.setDescontoProgressivo(null);
            descontoDTO.setDescontoMaiorValor(null);
            descontoDTO.setDescontoMenorValor(null);
            descontoDTO.setDescontoSimples(null);
            descontoDTO.setDescontoSimplesTermo(null);
            descontoDTO.setDescontoGenericoCarrinho(null);
            return this.cadastrarAtualizarDescontoFrete(descontoDTO);
        }
        else if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_GENERICO_CARRINHO)){
            descontoDTO.setDescontoProgressivo(null);
            descontoDTO.setDescontoMaiorValor(null);
            descontoDTO.setDescontoMenorValor(null);
            descontoDTO.setDescontoSimples(null);
            descontoDTO.setDescontoSimplesTermo(null);
            descontoDTO.setDescontoFrete(null);
            return this.cadastrarAtualizarDescontoGenericoCarrinho(descontoDTO);
        }
        else if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_SIMPLES_PRODUTO)){
            descontoDTO.setDescontoProgressivo(null);
            descontoDTO.setDescontoMaiorValor(null);
            descontoDTO.setDescontoMenorValor(null);
            descontoDTO.setDescontoSimplesTermo(null);
            descontoDTO.setDescontoFrete(null);
            descontoDTO.setDescontoGenericoCarrinho(null);
            return this.cadastrarAtualizarDescontoSimplesProduto(descontoDTO);
        }
        else if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_SIMPLES_TERMO)){
            descontoDTO.setDescontoProgressivo(null);
            descontoDTO.setDescontoMaiorValor(null);
            descontoDTO.setDescontoMenorValor(null);
            descontoDTO.setDescontoSimples(null);
            descontoDTO.setDescontoFrete(null);
            descontoDTO.setDescontoGenericoCarrinho(null);
            return this.cadastrarAtualizarDescontoSimplesTermo(descontoDTO);
        }
        else if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_PECA_MAIOR_VALOR)){
            descontoDTO.setDescontoProgressivo(null);
            descontoDTO.setDescontoSimplesTermo(null);
            descontoDTO.setDescontoMenorValor(null);
            descontoDTO.setDescontoSimples(null);
            descontoDTO.setDescontoFrete(null);
            descontoDTO.setDescontoGenericoCarrinho(null);
            return this.cadastrarAtualizarDescontoMaiorValor(descontoDTO);
        }
        else if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_PECA_MENOR_VALOR)){
            descontoDTO.setDescontoProgressivo(null);
            descontoDTO.setDescontoSimplesTermo(null);
            descontoDTO.setDescontoMaiorValor(null);
            descontoDTO.setDescontoSimples(null);
            descontoDTO.setDescontoFrete(null);
            descontoDTO.setDescontoGenericoCarrinho(null);
            return this.cadastrarAtualizarDescontoMenorValor(descontoDTO);
        }
        else if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_PROGRESSIVO)){
            descontoDTO.setDescontoSimplesTermo(null);
            descontoDTO.setDescontoMenorValor(null);
            descontoDTO.setDescontoMaiorValor(null);
            descontoDTO.setDescontoSimples(null);
            descontoDTO.setDescontoFrete(null);
            descontoDTO.setDescontoGenericoCarrinho(null);
            return this.cadastrarAtualizarDescontoProgressivo(descontoDTO);
        }
        return null;
    }

    public DescontoDTO cadastrarAtualizarDescontoProgressivo(DescontoDTO descontoDTO){
        Desconto entity = this.getUseCaseEntity(descontoDTO.getSystemId());
        this.arrumarEssencial(entity,descontoDTO);
        DescontoProgressivo descontoProgressivo = new DescontoProgressivo();
        if(entity.getDescontoProgressivo()!=null){
            descontoProgressivo = entity.getDescontoProgressivo();
        }
        descontoProgressivo.setDesconto(entity);
        descontoProgressivo.getIntervalos().clear();
        for(IntervalDescontoDTO interval:descontoDTO.getDescontoProgressivo().getIntervalos()){
            DescontoProgressivoIntervalos descontoProgressivoIntervalos = new DescontoProgressivoIntervalos();
            descontoProgressivoIntervalos.setMinQuantity(interval.getMinQuantity());
            descontoProgressivoIntervalos.setPercentDecimalDiscount(interval.getPercentDecimalDiscount());
            descontoProgressivoIntervalos.setDescontoProgressivo(descontoProgressivo);
        }
        entity.setDescontoProgressivo(descontoProgressivo);
        this.arrumarTermos(descontoDTO.getDescontoProgressivo(),entity);
        this.descontoRepository.save(entity);
        DescontoProgressivoDTO descontoProgressivoDTO = descontoDTO.getDescontoProgressivo();
        descontoProgressivoDTO.setSystemId(descontoProgressivo.getSystemId());
        descontoDTO.setDescontoProgressivo(descontoProgressivoDTO);
        return descontoDTO;
    }

    public DescontoDTO cadastrarAtualizarDescontoMenorValor(DescontoDTO descontoDTO){
        Desconto entity = this.getUseCaseEntity(descontoDTO.getSystemId());
        this.arrumarEssencial(entity,descontoDTO);
        DescontoMenorValor descontoMenorValor = new DescontoMenorValor();
        if(entity.getDescontoMenorValor()!=null){
            descontoMenorValor = entity.getDescontoMenorValor();
        }
        descontoMenorValor.setDesconto(entity);
        descontoMenorValor.setLowerQuantityLimitToApply(descontoDTO.getDescontoMenorValor().getLowerQuantityLimitToApply());
        descontoMenorValor.setPercentDecimalDiscount(descontoDTO.getDescontoMenorValor().getPercentDecimalDiscount());
        entity.setDescontoMenorValor(descontoMenorValor);
        this.arrumarTermos(descontoDTO.getDescontoMenorValor(),entity);
        this.descontoRepository.save(entity);
        DescontoMenorValorDTO descontoMenorValorDTO = descontoDTO.getDescontoMenorValor();
        descontoMenorValorDTO.setSystemId(descontoMenorValor.getSystemId());
        descontoDTO.setDescontoMenorValor(descontoMenorValorDTO);
        return descontoDTO;
    }

    public DescontoDTO cadastrarAtualizarDescontoMaiorValor(DescontoDTO descontoDTO){
        Desconto entity = this.getUseCaseEntity(descontoDTO.getSystemId());
        this.arrumarEssencial(entity,descontoDTO);
        DescontoMaiorValor descontoMaiorValor = new DescontoMaiorValor();
        if(entity.getDescontoMaiorValor()!=null){
            descontoMaiorValor = entity.getDescontoMaiorValor();
        }
        descontoMaiorValor.setDesconto(entity);
        descontoMaiorValor.setLowerQuantityLimitToApply(descontoDTO.getDescontoMaiorValor().getLowerQuantityLimitToApply());
        descontoMaiorValor.setPercentDecimalDiscount(descontoDTO.getDescontoMaiorValor().getPercentDecimalDiscount());
        entity.setDescontoMaiorValor(descontoMaiorValor);
        this.arrumarTermos(descontoDTO.getDescontoMaiorValor(),entity);
        this.descontoRepository.save(entity);
        DescontoMaiorValorDTO descontoMaiorValorDTO = descontoDTO.getDescontoMaiorValor();
        descontoMaiorValorDTO.setSystemId(descontoMaiorValor.getSystemId());
        descontoDTO.setDescontoMaiorValor(descontoMaiorValorDTO);
        return descontoDTO;
    }

    public DescontoDTO cadastrarAtualizarDescontoSimplesTermo(DescontoDTO descontoDTO){
        Desconto entity = this.getUseCaseEntity(descontoDTO.getSystemId());
        this.arrumarEssencial(entity,descontoDTO);
        DescontoSimplesTermo descontoSimplesTermo = new DescontoSimplesTermo();
        if(entity.getDescontoSimplesTermo()!=null){
            descontoSimplesTermo = entity.getDescontoSimplesTermo();
        }
        descontoSimplesTermo.setDesconto(entity);
        descontoSimplesTermo.setPercentDecimalDiscount(descontoDTO.getDescontoSimplesTermo().getPercentDecimalDiscount());
        entity.setDescontoSimplesTermo(descontoSimplesTermo);
        this.arrumarTermos(descontoDTO.getDescontoSimplesTermo(),entity);
        this.descontoRepository.save(entity);
        DescontoSimplesTermoDTO descontoSimplesTermoDTO = descontoDTO.getDescontoSimplesTermo();
        descontoSimplesTermoDTO.setSystemId(descontoSimplesTermo.getSystemId());
        descontoDTO.setDescontoSimplesTermo(descontoSimplesTermoDTO);
        return descontoDTO;
    }

    public DescontoDTO cadastrarAtualizarDescontoSimplesProduto(DescontoDTO descontoDTO){
        Desconto entity = this.getUseCaseEntity(descontoDTO.getSystemId());
        this.arrumarEssencial(entity,descontoDTO);
        DescontoSimplesProduto descontoSimplesProduto = new DescontoSimplesProduto();
        if(entity.getDescontoSimplesProduto()!=null){
            descontoSimplesProduto = entity.getDescontoSimplesProduto();
        }
        descontoSimplesProduto.setDesconto(entity);
        descontoSimplesProduto.setPercentDecimalDiscount(descontoDTO.getDescontoSimples().getPercentDecimalDiscount());
        Optional<ProdutoCatalogo> produtoCatalogoOptional = this.produtoCatalogoRepository.findById(descontoDTO.getDescontoSimples().getProduto().getSystemId());
        if(produtoCatalogoOptional.isEmpty()){
            throw new RuntimeException("Produto não encontrado");
        }
        ProdutoCatalogo produtoCatalogo = produtoCatalogoOptional.get();
        descontoSimplesProduto.setProdutoCatalogo(produtoCatalogo);
        entity.setDescontoSimplesProduto(descontoSimplesProduto);
        this.descontoRepository.save(entity);
        DescontoSimplesDTO descontoSimplesDTO = descontoDTO.getDescontoSimples();
        descontoSimplesDTO.setSystemId(descontoSimplesProduto.getSystemId());
        ProdutoDescontoDTO produtoDescontoDTO = descontoSimplesDTO.getProduto();
        produtoDescontoDTO.setSystemId(produtoCatalogo.getSystemId());
        produtoDescontoDTO.setNome(produtoCatalogo.getProdutoBaseFranquia().getDescricao());
        produtoDescontoDTO.setCatalogValue(produtoCatalogo.getCatalogPrice());
        produtoDescontoDTO.setBaseValue(produtoCatalogo.getProdutoBaseFranquia().getPreco());
        descontoSimplesDTO.setProduto(produtoDescontoDTO);
        descontoDTO.setDescontoSimples(descontoSimplesDTO);
        return descontoDTO;
    }

    public DescontoDTO cadastrarAtualizarDescontoGenericoCarrinho(DescontoDTO descontoDTO){
        Desconto entity = this.getUseCaseEntity(descontoDTO.getSystemId());
        this.arrumarEssencial(entity,descontoDTO);
        DescontoGenericoCarrinho descontoGenericoCarrinho = new DescontoGenericoCarrinho();
        if(entity.getDescontoGenericoCarrinho()!=null){
            descontoGenericoCarrinho = entity.getDescontoGenericoCarrinho();
        }
        descontoGenericoCarrinho.setDesconto(entity);
        descontoGenericoCarrinho.setPercentDecimalDiscount(descontoDTO.getDescontoGenericoCarrinho().getPercentDecimalDiscount());
        descontoGenericoCarrinho.setMinValue(descontoDTO.getDescontoGenericoCarrinho().getMinValue());
        entity.setDescontoGenericoCarrinho(descontoGenericoCarrinho);
        this.descontoRepository.save(entity);
        DescontoGenericoCarrinhoDTO descontoGenericoCarrinhoDTO = descontoDTO.getDescontoGenericoCarrinho();
        descontoGenericoCarrinhoDTO.setSystemId(descontoGenericoCarrinho.getSystemId());
        descontoDTO.setDescontoGenericoCarrinho(descontoGenericoCarrinhoDTO);
        return descontoDTO;
    }

    public DescontoDTO cadastrarAtualizarDescontoFrete(DescontoDTO descontoDTO){
        Desconto entity = this.getUseCaseEntity(descontoDTO.getSystemId());
        this.arrumarEssencial(entity,descontoDTO);
        DescontoFrete descontoFrete = new DescontoFrete();
        if(entity.getDescontoFrete()!=null){
            descontoFrete = entity.getDescontoFrete();
        }
        descontoFrete.setDesconto(entity);
        descontoFrete.setPercentDecimalDiscount(descontoDTO.getDescontoFrete().getPercentDecimalDiscount());
        descontoFrete.setLowerValueLimitToApply(descontoDTO.getDescontoFrete().getLowerValueLimitToApply());
        entity.setDescontoFrete(descontoFrete);
        this.descontoRepository.save(entity);
        DescontoFreteDTO descontoFreteDTO = descontoDTO.getDescontoFrete();
        descontoFreteDTO.setSystemId(descontoFrete.getSystemId());
        descontoDTO.setDescontoFrete(descontoFreteDTO);
        return descontoDTO;
    }


    private Desconto getUseCaseEntity(String id){
        if(id==null || id.isBlank()){
            return new Desconto();
        }
        Optional<Desconto> descontoOPT = this.descontoRepository.findById(id);
        return descontoOPT.orElseGet(Desconto::new);
    }

    private void arrumarTermos(DelimitedExcludedInterface object,Desconto entity){
        entity.getDelimitedTermos().clear();
        entity.getExcludedTermos().clear();
        for(String termo:object.getDelimitedCategorias()){
            DelimitedTermos delimitedTermos = new DelimitedTermos();
            delimitedTermos.setClassificacaoTermo(TermoTipo.CATEGORIA);
            delimitedTermos.setTermo(termo);
            entity.addDelimitedTermo(delimitedTermos);
        }
        for(String termo:object.getDelimitedLinhas()){
            DelimitedTermos delimitedTermos = new DelimitedTermos();
            delimitedTermos.setClassificacaoTermo(TermoTipo.LINHA);
            delimitedTermos.setTermo(termo);
            entity.addDelimitedTermo(delimitedTermos);
        }
        for(String termo:object.getDelimitedGrupos()){
            DelimitedTermos delimitedTermos = new DelimitedTermos();
            delimitedTermos.setClassificacaoTermo(TermoTipo.GRUPO);
            delimitedTermos.setTermo(termo);
            entity.addDelimitedTermo(delimitedTermos);
        }
        for(String termo:object.getExcludedCategorias()){
            ExcludedTermos excludedTermos = new ExcludedTermos();
            excludedTermos.setClassificacaoTermo(TermoTipo.CATEGORIA);
            excludedTermos.setTermo(termo);
            entity.addExcludedTermo(excludedTermos);
        }
        for(String termo:object.getExcludedLinhas()){
            ExcludedTermos excludedTermos = new ExcludedTermos();
            excludedTermos.setClassificacaoTermo(TermoTipo.LINHA);
            excludedTermos.setTermo(termo);
            entity.addExcludedTermo(excludedTermos);
        }
        for(String termo:object.getExcludedGrupos()){
            ExcludedTermos excludedTermos = new ExcludedTermos();
            excludedTermos.setClassificacaoTermo(TermoTipo.GRUPO);
            excludedTermos.setTermo(termo);
            entity.addExcludedTermo(excludedTermos);
        }
    }

    private void arrumarEssencial(Desconto entity,DescontoDTO descontoDTO){
        Optional<Loja> lojaOptional = this.lojaRepository.findById(descontoDTO.getLoja().getSystemId());
        if(lojaOptional.isEmpty()){
            throw new RuntimeException("Loja não encontrada");
        }
        entity.setDescontoTipo(descontoDTO.getTipo());
        entity.setDiscountName(descontoDTO.getNome());
        entity.setIsActive(descontoDTO.getIsActive());
        entity.setExpiresAt(descontoDTO.getExpiresAt());
        entity.setLoja(lojaOptional.get());
    }


}
