package com.skyler.catalogo.domain.lojas;


import com.skyler.catalogo.infra.auth.JwtService;
import com.skyler.catalogo.infra.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lojas")
public class LojaController {

    private final LojaService lojaService;
    private final JwtService jwtService;

    public LojaController(LojaService lojaService, JwtService jwtService) {
        this.lojaService = lojaService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity getLojas(){
        try{
            return ResponseEntity.ok().body(this.lojaService.getLojas());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }


    @PutMapping
    public ResponseEntity atualizarDadosMutaveisLoja(@RequestBody LojaChangePayload lojaTelefonePayload){
        try{
            this.lojaService.mudarLoja(lojaTelefonePayload);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }


    @GetMapping("/by-slug")
    public ResponseEntity getBySlug(String slug){
        try{
            return ResponseEntity.ok().body(this.lojaService.getBySlug(slug));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/by-id")
    public ResponseEntity getById(String id){
        try{
            return ResponseEntity.ok().body(this.lojaService.getById(id));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/by-franquia-id")
    public ResponseEntity getByFranquiaId(String franquiaId){
        try{
            return ResponseEntity.ok().body(this.lojaService.getLojasByFranquiaId(franquiaId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/matriz")
    public ResponseEntity getLojasMatriz(){
        try{
            return ResponseEntity.ok().body(this.lojaService.getLojasMatriz());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/franquia")
    public ResponseEntity getFranquias(){
        try{
            return ResponseEntity.ok().body(this.lojaService.getLojasFranquia());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
