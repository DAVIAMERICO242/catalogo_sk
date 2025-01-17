package com.skyler.catalogo.domain.produtos.services;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import com.skyler.catalogo.domain.produtos.DTOs.AtributosDTO;
import com.skyler.catalogo.domain.produtos.DTOs.ProdutoDTO;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FranquiaRepository franquiaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, FranquiaRepository franquiaRepository) {
        this.produtoRepository = produtoRepository;
        this.franquiaRepository = franquiaRepository;
    }

    public AtributosDTO getAtributos(String franquiaSystemId){
        Optional<Franquia> franquiaOPT = this.franquiaRepository.findById(franquiaSystemId);
        if(franquiaOPT.isEmpty()){
            throw new RuntimeException("Franquia não encontrada");
        }
        Franquia franquia = franquiaOPT.get();
        AtributosDTO atributos = new AtributosDTO();
        atributos.setCategorias(this.produtoRepository.getCategorias(franquia));
        atributos.setModelagens(this.produtoRepository.getModelagens(franquia));
        atributos.setGrupos(this.produtoRepository.getGrupos(franquia));
        return atributos;
    }

    public List<ProdutoDTO> getProdutos(String franquiaSystemId){
        List<ProdutoDTO> produtos = new ArrayList<>();
        Optional<Franquia> franquiaOPT = this.franquiaRepository.findById(franquiaSystemId);
        if(franquiaOPT.isEmpty()){
            throw new RuntimeException("Franquia não encontrada");
        }
        Franquia franquiaEnt = franquiaOPT.get();
        List<Produto> produtosEnt = this.produtoRepository.findAllByFranquia(franquiaEnt);
        for(Produto produtoEnt:produtosEnt){
            produtos.add(this.entityToDTO(produtoEnt));
        }
        return produtos;
    }


    public ProdutoDTO entityToDTO(Produto produtoEnt){
        ProdutoDTO produto = new ProdutoDTO();
        ProdutoDTO.Franquia franquia = new ProdutoDTO.Franquia();
        franquia.setFranquia(produtoEnt.getFranquia().getNome());
        franquia.setFranquiaSystemId(produtoEnt.getFranquia().getSystemId());
        produto.setFranquia(franquia);
        produto.setSystemId(produtoEnt.getSystemId());
        produto.setErpId(produtoEnt.getErpId());
        produto.setSku(produtoEnt.getSku());
        produto.setDescricao(produtoEnt.getDescricao());
        produto.setCategoria(produtoEnt.getCategoria());
        produto.setUnidade(produtoEnt.getUnidade());
        produto.setModelagem(produtoEnt.getModelagem());
        produto.setLinha(produtoEnt.getLinha());
        produto.setColecao(produtoEnt.getColecao());
        produto.setTipo(produtoEnt.getTipo());
        produto.setGrupo(produtoEnt.getGrupo());
        produto.setSubgrupo(produtoEnt.getSubgrupo());
        produto.setPreco(produtoEnt.getPreco());
        for(ProdutoVariacao variacaoEnt:produtoEnt.getVariacoes()){
            ProdutoDTO.Variacao variacao = new ProdutoDTO.Variacao();
            variacao.setSystemId(variacaoEnt.getSystemId());
            variacao.setSku(variacaoEnt.getSkuPonto());
            variacao.setErpId(variacaoEnt.getErpId());
            variacao.setPhotoUrl(variacaoEnt.getFotoUrl());
            variacao.setTamanho(variacaoEnt.getTamanho());
            variacao.setCor(variacaoEnt.getCor());
            produto.addVariacao(variacao);
        }
        return produto;
    }


}
