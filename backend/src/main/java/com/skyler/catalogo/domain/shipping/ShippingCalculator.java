package com.skyler.catalogo.domain.shipping;


import com.skyler.catalogo.domain.carrinho.CarrinhoRequest;
import com.skyler.catalogo.domain.correios.CorreiosFranquiaContext;
import com.skyler.catalogo.domain.correios.CorreiosFranquiaRepository;
import com.skyler.catalogo.domain.correios.apiCorreios.CorreiosAuth;
import com.skyler.catalogo.domain.correios.apiCorreios.CorreiosBridge;
import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.domain.pedidos.DTOs.PedidoBeforeCalculationsDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShippingCalculator {

    private final CorreiosFranquiaRepository correiosFranquiaRepository;
    private final CorreiosBridge correiosBridge;
    private final CorreiosAuth correiosAuth;
    private final LojaRepository lojaRepository;

    public ShippingCalculator(CorreiosFranquiaRepository correiosFranquiaRepository, CorreiosBridge correiosBridge, CorreiosAuth correiosAuth, LojaRepository lojaRepository) {
        this.correiosFranquiaRepository = correiosFranquiaRepository;
        this.correiosBridge = correiosBridge;
        this.correiosAuth = correiosAuth;
        this.lojaRepository = lojaRepository;
    }

    public FreteResponseDTO getFrete(
            PedidoBeforeCalculationsDTO pedidoBeforeCalculationsDTO
    ){
        Franquia franquia = this.lojaRepository.findById(pedidoBeforeCalculationsDTO.getLoja().getSystemId()).get().getFranquia();
        Optional<CorreiosFranquiaContext> correiosFranquiaContextOptional = this.correiosFranquiaRepository.findByFranquiaId(franquia.getSystemId());
        if(correiosFranquiaContextOptional.isEmpty()){
            return new FreteResponseDTO(
                    null,
                    null
            );
        }
        CorreiosFranquiaContext correiosFranquiaContext = correiosFranquiaContextOptional.get();
        String token = this.correiosAuth.getToken(
                correiosFranquiaContext.getNumeroContrato(),
                correiosFranquiaContext.getNumeroCartaoPostal(),
                correiosFranquiaContext.getUsuario(),
                correiosFranquiaContext.getSenha()
        );
        Float valorFrete = this.correiosBridge.getPrecoFrete(
                token,
                correiosFranquiaContext.getCodigoPac(),
                correiosFranquiaContext.getNumeroContrato(),
                correiosFranquiaContext.getNumeroDiretoriaRegional(),
                correiosFranquiaContext.getCepOrigem(),
                pedidoBeforeCalculationsDTO.getCep(),
                0,
                0,
                0,
                0
            );
        Integer prazoFrete = this.correiosBridge.getPrazoFrete(
                token,
                correiosFranquiaContext.getCodigoPac(),
                correiosFranquiaContext.getNumeroContrato(),
                correiosFranquiaContext.getNumeroDiretoriaRegional(),
                correiosFranquiaContext.getCepOrigem(),
                pedidoBeforeCalculationsDTO.getCep(),
                0,
                0,
                0,
                0
        );

        return new FreteResponseDTO(
                valorFrete,
                prazoFrete
        );

    }
}
