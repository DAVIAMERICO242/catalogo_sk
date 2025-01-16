package com.skyler.catalogo.infra.integrador;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import com.skyler.catalogo.domain.produtos.Produto;
import com.skyler.catalogo.domain.produtos.ProdutoRepository;
import com.skyler.catalogo.domain.produtos.ProdutoVariacao;
import com.skyler.catalogo.infra.integrador.DTOs.ProdutoIntegrador;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IntegradorProdutos extends IntegradorContext {

    private final IntegradorBridge integradorBridge;
    private final ProdutoRepository produtoRepository;
    private final FranquiaRepository franquiaRepository;

    public IntegradorProdutos(IntegradorBridge integradorBridge, ProdutoRepository produtoRepository, FranquiaRepository franquiaRepository) {
        this.integradorBridge = integradorBridge;
        this.produtoRepository = produtoRepository;
        this.franquiaRepository = franquiaRepository;
    }

    public void updateProdutos(){
        List<ProdutoIntegrador> produtos = integradorBridge.getProdutos();
        List<Produto> produtosEnt = new ArrayList<>();
        for(ProdutoIntegrador produto:produtos){
            Optional<Franquia> franquiaOPT = this.franquiaRepository.findByIntegradorId(produto.getFranquiaIntegradorId());
            if(franquiaOPT.isEmpty()){
                continue;
            }
            Produto produtoEnt = new Produto();
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
            produtosEnt.add(produtoEnt);
        }
        this.produtoRepository.saveAll(produtosEnt);
    }
}
