package com.skyler.catalogo.domain.pedidos.controllers;

import com.skyler.catalogo.domain.pedidos.DTOs.PedidoBeforeCalculationsDTO;
import com.skyler.catalogo.domain.pedidos.Pedido;
import com.skyler.catalogo.domain.pedidos.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity getPedidos(@RequestParam(required = false) String lojaId, @RequestParam(required = true) String franquiaId){
        try{
            return ResponseEntity.ok().body(this.pedidoService.getPedidos(lojaId,franquiaId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PutMapping("/mudar-status")
    public ResponseEntity mudarStatus(String pedidoId){
        try{
            this.pedidoService.mudarStatus(pedidoId);
            return ResponseEntity.status(200).build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PostMapping
    public ResponseEntity postarPedido(@RequestBody PedidoBeforeCalculationsDTO pedidoBeforeCalculationsDTO){
        try{
            this.pedidoService.novoPedido(pedidoBeforeCalculationsDTO);
            return ResponseEntity.status(200).build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
