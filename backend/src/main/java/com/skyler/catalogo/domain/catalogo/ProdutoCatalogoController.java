package com.skyler.catalogo.domain.catalogo;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/catalogo")
public class ProdutoCatalogoController {

    private final ProdutoCatalogoService produtoCatalogoService;
    private final ProdutoCatalogoRepository produtoCatalogoRepository;

    public ProdutoCatalogoController(ProdutoCatalogoService produtoCatalogoService, ProdutoCatalogoRepository produtoCatalogoRepository) {
        this.produtoCatalogoService = produtoCatalogoService;
        this.produtoCatalogoRepository = produtoCatalogoRepository;
    }

    @PostMapping
    public ResponseEntity postProdutoCatalogo(@RequestBody ProdutoCadastroDTO payload){
        try{
            return ResponseEntity.ok().body(this.produtoCatalogoService.cadastrarProdutoCatalogo(payload));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody ProdutoDelecaoDTO payload){
        try{
            this.produtoCatalogoRepository.deleteByProductIdAndLojaSlug(payload.getSystemId(), payload.getLojaSlug());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping
    public ResponseEntity getCatalogo(String lojaSlug){
        try{
            return ResponseEntity.ok().body(this.produtoCatalogoService.getProdutosCatalogo(lojaSlug));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
