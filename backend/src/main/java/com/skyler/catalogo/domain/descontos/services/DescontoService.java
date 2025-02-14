package com.skyler.catalogo.domain.descontos.services;

import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogoRepository;
import com.skyler.catalogo.domain.descontos.DTOs.*;

import com.skyler.catalogo.domain.descontos.enums.DescontoTipo;
import com.skyler.catalogo.domain.descontos.enums.TermoTipo;
import com.skyler.catalogo.domain.descontos.interfaces.DelimitedExcludedInterface;
import com.skyler.catalogo.domain.descontos.repositories.DescontoRepository;
import com.skyler.catalogo.domain.descontos.entities.*;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public void deletarDesconto(String id){
        if(this.descontoRepository.numeroAplicacoes(id)>0){
            throw new RuntimeException("Existem pedidos associados a esse desconto.");
        }
        this.descontoRepository.deleteById(id);
    }

    public List<DescontoDTO> getDescontosAtivosByLojaSlug(String slug){
        List<DescontoDTO> descontoDTOList = new ArrayList<>();
        List<Desconto> descontos = new ArrayList<>();
        descontos = this.descontoRepository.findAllActiveAndNotExpiredByLojaId(LocalDate.now(),this.lojaRepository.findByLojaSlug(slug).get().getSystemId());
        for(Desconto desconto:descontos){
            descontoDTOList.add(this.descontoMapper.entityToDTO(desconto));
        }
        descontoDTOList.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
        return descontoDTOList;
    }

    public List<DescontoDTO> getDescontos(String lojaSystemId, String franquiaSystemId){
        List<DescontoDTO> descontoDTOList = new ArrayList<>();
        List<Desconto> descontos = new ArrayList<>();
        if(lojaSystemId!=null && !lojaSystemId.isBlank()){
            descontos = this.descontoRepository.findAllByLojaIn(this.lojaRepository.findById(lojaSystemId).get());
        }else{
            descontos = this.descontoRepository.findAllByFranquiaId(franquiaSystemId);
        }
        for(Desconto desconto:descontos){
            descontoDTOList.add(this.descontoMapper.entityToDTO(desconto));
        }
        descontoDTOList.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
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
            descontoProgressivoIntervalos.setDescontoProgressivo(descontoProgressivo);
            descontoProgressivo.addInterval(descontoProgressivoIntervalos);
        }
        entity.setDescontoProgressivo(descontoProgressivo);
        this.arrumarTermos(descontoDTO.getDescontoProgressivo(),entity);
        this.descontoRepository.save(entity);
        DescontoProgressivoDTO descontoProgressivoDTO = descontoDTO.getDescontoProgressivo();
        descontoProgressivoDTO.setSystemId(descontoProgressivo.getSystemId());
        descontoDTO.setSystemId(entity.getSystemId());
        descontoDTO.setDescontoProgressivo(descontoProgressivoDTO);
        descontoDTO.setCreatedAt(entity.getCreatedAt());
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
        descontoDTO.setSystemId(entity.getSystemId());
        descontoDTO.setDescontoMenorValor(descontoMenorValorDTO);
        descontoDTO.setCreatedAt(entity.getCreatedAt());
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
        descontoDTO.setSystemId(entity.getSystemId());
        descontoDTO.setDescontoMaiorValor(descontoMaiorValorDTO);
        descontoDTO.setCreatedAt(entity.getCreatedAt());
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
        descontoDTO.setSystemId(entity.getSystemId());
        descontoDTO.setDescontoSimplesTermo(descontoSimplesTermoDTO);
        descontoDTO.setCreatedAt(entity.getCreatedAt());
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
        produtoDescontoDTO.setBaseValue(produtoCatalogo.getProdutoBaseFranquia().getPreco());
        descontoSimplesDTO.setProduto(produtoDescontoDTO);
        descontoDTO.setSystemId(entity.getSystemId());
        descontoDTO.setDescontoSimples(descontoSimplesDTO);
        descontoDTO.setCreatedAt(entity.getCreatedAt());
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
        descontoDTO.setSystemId(entity.getSystemId());
        descontoDTO.setDescontoGenericoCarrinho(descontoGenericoCarrinhoDTO);
        descontoDTO.setCreatedAt(entity.getCreatedAt());
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
        descontoDTO.setSystemId(entity.getSystemId());
        descontoDTO.setDescontoFrete(descontoFreteDTO);
        descontoDTO.setCreatedAt(entity.getCreatedAt());
        return descontoDTO;
    }


    private Desconto getUseCaseEntity(String id){
        if(id==null || id.isBlank()){
            return new Desconto();
        }
        Optional<Desconto> descontoOPT = this.descontoRepository.findById(id);
        return descontoOPT.orElseGet(Desconto::new);
    }

    private void arrumarTermos(DelimitedExcludedInterface object, Desconto entity){
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
        List<Loja> lojas = this.lojaRepository.findAllById(descontoDTO.getLojas().stream().map(o->o.getSystemId()).toList());
        entity.setDescontoTipo(descontoDTO.getTipo());
        entity.setDiscountName(descontoDTO.getNome());
        entity.setIsActive(descontoDTO.getIsActive());
        entity.setExpiresAt(descontoDTO.getExpiresAt());
        entity.getLojas().clear();
        for(Loja loja:lojas){
            entity.addLoja(loja);
        }
    }


}
