package com.skyler.catalogo.domain.catalogo;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.domain.produtos.DTOs.ProdutoDTO;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoRepository;
import com.skyler.catalogo.domain.produtos.services.ProdutoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoCatalogoService {

    private final ProdutoCatalogoRepository produtoCatalogoRepository;
    private final ProdutoService produtoService;
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;

    public ProdutoCatalogoService(ProdutoCatalogoRepository produtoCatalogoRepository, ProdutoService produtoService, ProdutoRepository produtoRepository, LojaRepository lojaRepository) {
        this.produtoCatalogoRepository = produtoCatalogoRepository;
        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
        this.lojaRepository = lojaRepository;
    }

//    public void changePrecoCatalogo(Float preco, String produtoCatalogoId){
//        Optional<ProdutoCatalogo> catalogo = this.produtoCatalogoRepository.findById(produtoCatalogoId);
//        if(catalogo.isEmpty()){
//            throw new RuntimeException("Produto não encontrado");
//        }
//        ProdutoCatalogo produtoCatalogo = catalogo.get();
//        produtoCatalogo.setCatalogPrice(preco);
//        this.produtoCatalogoRepository.save(produtoCatalogo);
//    }

    public ProdutoCatalogoDTO getProduct(String id){
        Optional<ProdutoCatalogo> produtoCatalogoOptional = this.produtoCatalogoRepository.findById(id);
        if(produtoCatalogoOptional.isEmpty()){
            throw new RuntimeException("Esse produto não está mais disponível no catálogo");
        }
        return this.entityToDTO(produtoCatalogoOptional.get(),true);
    }

    @Transactional
    public ProdutoCatalogoDTO cadastrarProdutoCatalogo(ProdutoCadastroDTO payload){
        Optional<Produto> produtoOPT = this.produtoRepository.findById(payload.getSystemId());
        if(produtoOPT.isEmpty()){
            throw new RuntimeException("Produto não encontrado");
        }
        Produto produto = produtoOPT.get();
        Franquia franquia = produto.getFranquia();
        Optional<Loja> lojaOPT = this.lojaRepository.findByLojaSlug(payload.getLojaSlug());
        if(lojaOPT.isEmpty()){
            throw new RuntimeException("Nenhuma loja com esse slug encontrada");
        }
        if(franquia == null){
            throw new RuntimeException("Produto sem franquia");
        }
        if(this.produtoCatalogoRepository.findByProdutoIdAndLojaSlug(payload.getSystemId(),payload.getLojaSlug()).isPresent()){
            throw new RuntimeException("Esse produto já está no catalogo dessa loja");
        }
        Loja loja = lojaOPT.get();
        if(!franquia.getLojas().contains(loja)){
            throw new RuntimeException("O slug da loja informada não pertence a uma loja da franquia desse produto! ");
        }
        ProdutoCatalogo produtoCatalogo = new ProdutoCatalogo();
        produtoCatalogo.setLoja(loja);
        produtoCatalogo.setProdutoBaseFranquia(produto);
//        produtoCatalogo.setCatalogPrice(produto.getPreco());
        return this.entityToDTO(this.produtoCatalogoRepository.save(produtoCatalogo),false);
    }

    public List<ProdutoCatalogoDTO> getProdutosCatalogo(String lojaSlug, Boolean loadVariations){
        List<ProdutoCatalogoDTO> output = new ArrayList<>();
        List<ProdutoCatalogo> catalogosEnt = null;
        if(loadVariations==null || !loadVariations){
            catalogosEnt = this.produtoCatalogoRepository.findAllByLojaWithoutVariacoesLoaded(lojaSlug);
        }else{
            catalogosEnt = this.produtoCatalogoRepository.findAllByLojaSlug(lojaSlug);
        }
        for(ProdutoCatalogo catalogoEnt:catalogosEnt){
            output.add(this.entityToDTO(catalogoEnt,Optional.ofNullable(loadVariations).orElse(false)));
        }
        return output;
    }

    public ProdutoCatalogoDTO entityToDTO(ProdutoCatalogo catalogoEnt,Boolean withVariations){
        ProdutoCatalogoDTO produtoCatalogo = new ProdutoCatalogoDTO();
        Produto produtoEnt = catalogoEnt.getProdutoBaseFranquia();
        ProdutoDTO produto = this.produtoService.entityToDTOExistingOnCatalogo(produtoEnt);
        ProdutoCatalogoDTO.Loja loja = new ProdutoCatalogoDTO.Loja();
        loja.setLoja(catalogoEnt.getLoja().getNome());
        loja.setSystemId(catalogoEnt.getLoja().getSystemId());
        loja.setSlug(catalogoEnt.getLoja().getSlug());
        produtoCatalogo.setSystemId(catalogoEnt.getSystemId());
        produtoCatalogo.setProdutoBase(produto);
        produtoCatalogo.setLojaCatalogo(loja);
        if(withVariations){
            for(ProdutoVariacao variacaoEnt:produtoEnt.getVariacoes()){
                ProdutoDTO.Variacao variacao = new ProdutoDTO.Variacao();
                variacao.setSystemId(variacaoEnt.getSystemId());
                variacao.setSku(variacaoEnt.getSkuPonto());
                variacao.setCor(variacaoEnt.getCor());
                variacao.setTamanho(variacaoEnt.getTamanho());
                variacao.setFoto(variacaoEnt.getFotoUrl());
                produto.addVariacao(variacao);
            }
        }
        return produtoCatalogo;
    }


}
