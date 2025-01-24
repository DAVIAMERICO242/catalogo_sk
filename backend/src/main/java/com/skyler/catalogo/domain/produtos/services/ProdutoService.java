package com.skyler.catalogo.domain.produtos.services;

import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogoDTO;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogoRepository;
import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.domain.produtos.DTOs.*;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoRepository;
import com.skyler.catalogo.domain.produtos.specifications.ProdutoSpecifications;
import com.skyler.catalogo.infra.integrador.IntegradorBridge;
import com.skyler.catalogo.infra.integrador.IntegradorEstoque;
import com.skyler.catalogo.infra.integrador.IntegradorFranquiasELojas;
import com.skyler.catalogo.infra.integrador.IntegradorProdutos;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FranquiaRepository franquiaRepository;
    private final LojaRepository lojaRepository;
    private final IntegradorBridge integradorBridge;
    private final ProdutoCatalogoRepository produtoCatalogoRepository;


    public ProdutoService(ProdutoRepository produtoRepository, FranquiaRepository franquiaRepository, LojaRepository lojaRepository, IntegradorBridge integradorBridge, ProdutoCatalogoRepository produtoCatalogoRepository) {
        this.produtoRepository = produtoRepository;
        this.franquiaRepository = franquiaRepository;
        this.lojaRepository = lojaRepository;
        this.integradorBridge = integradorBridge;
        this.produtoCatalogoRepository = produtoCatalogoRepository;
    }

    public List<ProdutoEstoqueDTO> getEstoque(List<String> skusBase, String lojaSlug){
        List<ProdutoEstoqueDTO> output = new ArrayList<>();
        Optional<Loja> lojaOPT = this.lojaRepository.findByLojaSlug(lojaSlug);
        if(lojaOPT.isEmpty()){
            throw new RuntimeException("Slug da loja não encontrado");
        }
        Loja loja = lojaOPT.get();
        List<IntegradorEstoque> data = this.integradorBridge.getEstoque(skusBase,loja.getIntegradorId());
        for(IntegradorEstoque estoque:data){
            ProdutoEstoqueDTO instance = new ProdutoEstoqueDTO();
            instance.setSku(estoque.getSku());
            for(IntegradorEstoque.Variacao variacao:estoque.getVariacoes()){
                ProdutoEstoqueDTO.VariacaoEstoque estoqueDTO = new ProdutoEstoqueDTO.VariacaoEstoque();
                estoqueDTO.setSku(variacao.getSku());
                estoqueDTO.setEstoque(variacao.getEstoque());
                instance.addVariacao(estoqueDTO);
            }
            output.add(instance);
        }
        return output;
    }

    public TermoDTO getTermos(String franquiaSystemId){
        TermoDTO output = new TermoDTO();
        Optional<Franquia> franquiaOPT = this.franquiaRepository.findById(franquiaSystemId);
        if(franquiaOPT.isEmpty()){
            throw new RuntimeException("Franquia não encontrada");
        }
        output.setCategorias(this.produtoRepository.getCategorias(franquiaOPT.get()));
        output.setLinhas(this.produtoRepository.getLinhas(franquiaOPT.get()));
        output.setGrupos(this.produtoRepository.getGrupos(franquiaOPT.get()));
        return output;
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

    public Page<ProdutoDTO> getProdutos(Integer page,
                                        String lojaSlug,
                                        String nome,
                                        String sku
    ){
        List<ProdutoDTO> produtos = new ArrayList<>();
        Optional<Loja> loja = this.lojaRepository.findByLojaSlug(lojaSlug);
        if(loja.isEmpty()){
            throw new RuntimeException("Loja não encontrada");
        }
        Franquia franquiaEnt = loja.get().getFranquia();
        if(franquiaEnt==null){
            throw new RuntimeException("Loja sem franquia");
        }
        List<ProdutoCatalogo> catalogo = this.produtoCatalogoRepository.findAllByLojaSlug(lojaSlug);
        PageRequest pageRequest = PageRequest.of(page,50);
        Page<Produto> produtosEnt = this.produtoRepository.findAll(
                ProdutoSpecifications.hasFranquiaWithoutVariacoes(franquiaEnt)
                        .and(ProdutoSpecifications.nomeContains(nome))
                        .and(ProdutoSpecifications.skuContains(sku))
                ,pageRequest);
        for(Produto produtoEnt:produtosEnt){
//            Hibernate.initialize(produtoEnt.getVariacoes());
//            Hibernate.initialize(produtoEnt.getFranquia());
            produtos.add(this.entityToDTO(produtoEnt,catalogo));
        }
        return new PageImpl<>(produtos, pageRequest, produtosEnt.getTotalElements());
    }

    public List<ProdutoVariacaoDTO> getVariacao(String productId){
        List<ProdutoVariacao> variacoes = this.produtoRepository.findVariacoesForProduct(productId);
        List<ProdutoVariacaoDTO> output = new ArrayList<>();
        for(ProdutoVariacao variacao:variacoes){
            ProdutoVariacaoDTO instance = new ProdutoVariacaoDTO();
            instance.setCor(variacao.getCor());
            instance.setTamanho(variacao.getTamanho());
            instance.setFoto(variacao.getFotoUrl());
            instance.setErpId(variacao.getErpId());
            instance.setIntegradorId(variacao.getProdutoVariacaoIntegradorId());
            instance.setSku(variacao.getSkuPonto());
            instance.setSystemId(variacao.getSystemId());
            output.add(instance);
        }
        return output;
    }


    public ProdutoDTO entityToDTO(Produto produtoEnt,List<ProdutoCatalogo> catalogo){
        ProdutoDTO produto = new ProdutoDTO();
        ProdutoDTO.Franquia franquia = new ProdutoDTO.Franquia();
        Boolean onCatalogo = catalogo.stream().anyMatch(o->o.getProdutoBaseFranquia().equals(produtoEnt));
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
        produto.setPhotoUrl(produtoEnt.getPhotoUrl());
        produto.setOnCatalogo(onCatalogo);
        return produto;
    }

    public ProdutoDTO entityToDTOExistingOnCatalogo(Produto produtoEnt){
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
        produto.setPhotoUrl(produtoEnt.getPhotoUrl());
        produto.setOnCatalogo(true);
        return produto;
    }


}
