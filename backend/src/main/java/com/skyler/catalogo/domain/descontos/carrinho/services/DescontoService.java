package com.skyler.catalogo.domain.descontos.carrinho.services;

import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogoRepository;
import com.skyler.catalogo.domain.descontos.carrinho.DTOs.*;
import com.skyler.catalogo.domain.descontos.carrinho.entities.*;
import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.repositories.DescontoRepository;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DescontoService {

    private final LojaRepository lojaRepository;
    private final ProdutoCatalogoRepository produtoCatalogoRepository;
    private final DescontoRepository descontoRepository;

    public DescontoService(LojaRepository lojaRepository, ProdutoCatalogoRepository produtoCatalogoRepository, DescontoRepository descontoRepository) {
        this.lojaRepository = lojaRepository;
        this.produtoCatalogoRepository = produtoCatalogoRepository;
        this.descontoRepository = descontoRepository;
    }

    public DescontoDTO cadastrarAtualizarDesconto(DescontoDTO descontoDTO){
        if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_FRETE)){
            return this.cadastrarAtualizarDescontoFrete(descontoDTO);
        }
        if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_GENERICO_CARRINHO)){
            return this.cadastrarAtualizarDescontoGenericoCarrinho(descontoDTO);
        }
        if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_SIMPLES_PRODUTO)){
            return this.cadastrarAtualizarDescontoSimplesProduto(descontoDTO);
        }
        if(descontoDTO.getTipo().equals(DescontoTipo.DESCONTO_SIMPLES_TERMO)){

        }
    }

    public DescontoDTO cadastrarAtualizarDescontoSimplesTermo(DescontoDTO descontoDTO){
        Desconto entity = this.getUseCaseEntity(descontoDTO.getSystemId());
        DescontoSimplesTermo descontoSimplesTermo = new DescontoSimplesTermo();
        if(entity.getDescontoSimplesTermo()!=null){
            descontoSimplesTermo = entity.getDescontoSimplesTermo();
        }
        entity.getDelimitedTermos().clear();
        entity.getExcludedTermos().clear();
        
    }

    public DescontoDTO cadastrarAtualizarDescontoSimplesProduto(DescontoDTO descontoDTO){
        Desconto entity = this.getUseCaseEntity(descontoDTO.getSystemId());
        DescontoSimplesProduto descontoSimplesProduto = new DescontoSimplesProduto();
        if(entity.getDescontoSimplesProduto()!=null){
            descontoSimplesProduto = entity.getDescontoSimplesProduto();
        }
        descontoSimplesProduto.setPercentDecimalDiscount(descontoDTO.getDescontoFrete().getPercentDecimalDiscount());
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
        DescontoGenericoCarrinho descontoGenericoCarrinho = new DescontoGenericoCarrinho();
        if(entity.getDescontoGenericoCarrinho()!=null){
            descontoGenericoCarrinho = entity.getDescontoGenericoCarrinho();
        }
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
        DescontoFrete descontoFrete = new DescontoFrete();
        if(entity.getDescontoFrete()!=null){
            descontoFrete = entity.getDescontoFrete();
        }
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


}
