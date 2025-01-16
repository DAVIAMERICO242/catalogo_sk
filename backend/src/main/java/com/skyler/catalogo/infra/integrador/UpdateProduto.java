package com.skyler.catalogo.infra.integrador;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoRepository;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.infra.integrador.DTOs.ProdutoIntegrador;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateProduto {
    private final FranquiaRepository franquiaRepository;
    private final ProdutoRepository produtoRepository;

    public UpdateProduto(FranquiaRepository franquiaRepository, ProdutoRepository produtoRepository) {
        this.franquiaRepository = franquiaRepository;
        this.produtoRepository = produtoRepository;
    }

    @Async
    @Transactional
    public void updateProduto(ProdutoIntegrador produto){
        Optional<Franquia> franquiaOPT = this.franquiaRepository.findByIntegradorId(produto.getFranquiaIntegradorId());
        if(franquiaOPT.isEmpty()){
            return;
        }
        Produto produtoEnt = new Produto();
        Optional<Produto> produtoOptional = this.produtoRepository.findByIntegradorId(produto.getIntegradorId());
        if(produtoOptional.isPresent()){
            produtoEnt = produtoOptional.get();
        }
        produtoEnt.setProdutoIntegradorId(produto.getIntegradorId());
        produtoEnt.setErpId(produto.getErpId());
        produtoEnt.setSku(produto.getSku());
        produtoEnt.setDescricao(produto.getDescricao());
        produtoEnt.setCategoria(produto.getCategoria());
        produtoEnt.setModelagem(produto.getModelagem());
        produtoEnt.setUnidade(produto.getUnidade());
        produtoEnt.setLinha(produto.getLinha());
        produtoEnt.setColecao(produto.getColecao());
        produtoEnt.setTipo(produto.getTipo());
        produtoEnt.setGrupo(produto.getGrupo());
        produtoEnt.setSubgrupo(produto.getSubgrupo());
        produtoEnt.setPreco(produto.getPreco());
        produtoEnt.setFranquia(franquiaOPT.get());
        for(ProdutoIntegrador.Variacao variacao:produto.getVariacoes()){
            ProdutoVariacao variacaoEnt = new ProdutoVariacao();
            variacaoEnt.setProdutoVariacaoIntegradorId(variacao.getIntegradorId());
            variacaoEnt.setErpId(variacao.getErpId());
            variacaoEnt.setSkuPonto(variacao.getSku());
            variacaoEnt.setCor(variacao.getCor());
            variacaoEnt.setTamanho(variacao.getTamanho());
            variacaoEnt.setFotoUrl(variacao.getPhotoUrl());
            produtoEnt.addOrUpdateVariacao(variacaoEnt);
        }
        this.produtoRepository.save(produtoEnt);
    }
}
