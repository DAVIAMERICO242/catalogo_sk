package com.skyler.catalogo.domain.catalogo;

import com.skyler.catalogo.domain.produtos.DTOs.ProdutoDTO;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.services.ProdutoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProdutoCatalogoService {

    private final ProdutoCatalogoRepository produtoCatalogoRepository;
    private final ProdutoService produtoService;

    public ProdutoCatalogoService(ProdutoCatalogoRepository produtoCatalogoRepository, ProdutoService produtoService) {
        this.produtoCatalogoRepository = produtoCatalogoRepository;
        this.produtoService = produtoService;
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
