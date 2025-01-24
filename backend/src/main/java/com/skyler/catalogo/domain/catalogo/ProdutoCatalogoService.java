package com.skyler.catalogo.domain.catalogo;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
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
    private final LojaRepository lojaRepository;

    public ProdutoCatalogoService(ProdutoCatalogoRepository produtoCatalogoRepository, ProdutoService produtoService, ProdutoRepository produtoRepository, LojaRepository lojaRepository) {
        this.produtoCatalogoRepository = produtoCatalogoRepository;
        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
        this.lojaRepository = lojaRepository;
    }

    public void changePrecoCatalogo(Float preco, String produtoCatalogoId){
        Optional<ProdutoCatalogo> catalogo = this.produtoCatalogoRepository.findById(produtoCatalogoId);
        if(catalogo.isEmpty()){
            throw new RuntimeException("Produto não encontrado");
        }
        ProdutoCatalogo produtoCatalogo = catalogo.get();
        produtoCatalogo.setCatalogPrice(preco);
        this.produtoCatalogoRepository.save(produtoCatalogo);
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
        produtoCatalogo.setCatalogPrice(produto.getPreco());
        return this.entityToDTO(this.produtoCatalogoRepository.save(produtoCatalogo));
    }

    public List<ProdutoCatalogoDTO> getProdutosCatalogo(String lojaSlug){
        List<ProdutoCatalogoDTO> output = new ArrayList<>();
        List<ProdutoCatalogo> catalogosEnt = this.produtoCatalogoRepository.findAllByLojaSlug(lojaSlug);
        for(ProdutoCatalogo catalogoEnt:catalogosEnt){
            output.add(this.entityToDTO(catalogoEnt));
        }
        return output;
    }

    public ProdutoCatalogoDTO entityToDTO(ProdutoCatalogo catalogoEnt){
        ProdutoCatalogoDTO produtoCatalogo = new ProdutoCatalogoDTO();
        Produto produtoEnt = catalogoEnt.getProdutoBaseFranquia();
        ProdutoDTO produto = this.produtoService.entityToDTOExistingOnCatalogo(produtoEnt);
        ProdutoCatalogoDTO.Loja loja = new ProdutoCatalogoDTO.Loja();
        loja.setLoja(catalogoEnt.getLoja().getNome());
        loja.setSystemId(catalogoEnt.getLoja().getSystemId());
        loja.setSlug(catalogoEnt.getLoja().getSlug());
        produtoCatalogo.setSystemId(catalogoEnt.getSystemId());
        produtoCatalogo.setValorCatalogo(catalogoEnt.getCatalogPrice());
        produtoCatalogo.setProdutoBase(produto);
        produtoCatalogo.setLojaCatalogo(loja);
        return produtoCatalogo;
    }


}
