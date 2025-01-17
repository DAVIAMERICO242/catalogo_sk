package com.skyler.catalogo.domain.catalogo;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.produtos.DTOs.ProdutoDTO;
import com.skyler.catalogo.domain.produtos.entities.Produto;
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

    public ProdutoCatalogoService(ProdutoCatalogoRepository produtoCatalogoRepository, ProdutoService produtoService, ProdutoRepository produtoRepository) {
        this.produtoCatalogoRepository = produtoCatalogoRepository;
        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public void cadastrarProdutoCatalogo(ProdutoCadastroDTO payload){
        Optional<Produto> produtoOPT = this.produtoRepository.findById(payload.getSystemId());
        if(produtoOPT.isEmpty()){
            throw new RuntimeException("Produto não encontrado");
        }
        Produto produto = produtoOPT.get();
        Franquia franquia = produto.getFranquia();
        if(franquia == null){
            throw new RuntimeException("Produto sem franquia");
        }
        if(this.produtoCatalogoRepository.findByProdutoIdAndLojaSlug(payload.getSystemId(),payload.getLojaSlug()).isPresent()){
            throw new RuntimeException("Esse produto já está no catalogo dessa loja");
        }
        List<String> slugs = franquia.getLojas().stream().map(o->o.getSlug()).toList();
        if(!slugs.contains(payload.getLojaSlug())){
            throw new RuntimeException("O slug da loja informada não pertence a uma loja da franquia desse produto! ");
        }

    }

    public List<ProdutoCatalogoDTO> getProdutosCatalogo(String lojaSlug){
        List<ProdutoCatalogoDTO> output = new ArrayList<>();
        List<ProdutoCatalogo> catalogosEnt = this.produtoCatalogoRepository.findAllByLojaSlug(lojaSlug);
        for(ProdutoCatalogo catalogoEnt:catalogosEnt){
            ProdutoCatalogoDTO produtoCatalogo = new ProdutoCatalogoDTO();
            Produto produtoEnt = catalogoEnt.getProdutoBaseFranquia();
            ProdutoDTO produto = this.produtoService.entityToDTO(produtoEnt);
            ProdutoCatalogoDTO.Loja loja = new ProdutoCatalogoDTO.Loja();
            loja.setLoja(catalogoEnt.getLoja().getNome());
            loja.setSystemId(catalogoEnt.getLoja().getSystemId());
            loja.setSlug(catalogoEnt.getLoja().getSlug());
            produtoCatalogo.setValorCatalogo(catalogoEnt.getCatalogPrice());
            produtoCatalogo.setProdutoBase(produto);
            produtoCatalogo.setLojaCatalogo(loja);
            output.add(produtoCatalogo);
        }
        return output;
    }


}
