package com.skyler.catalogo.domain.shipping.correios;

import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import com.skyler.catalogo.domain.shipping.correios.apiCorreios.CorreiosAuth;
import com.skyler.catalogo.domain.shipping.correios.apiCorreios.CorreiosBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GambiarraPraNaoFicarLento {

    private final CorreiosFranquiaRepository correiosFranquiaRepository;
    private final CorreiosAuth correiosAuth;

    public GambiarraPraNaoFicarLento(CorreiosFranquiaRepository correiosFranquiaRepository, CorreiosAuth correiosAuth) {
        this.correiosFranquiaRepository = correiosFranquiaRepository;
        this.correiosAuth = correiosAuth;
    }

    @Scheduled(cron = "0 * * * * *")
    public void priorizarNoSistemaDosCorreios(){//quando uma requisição para os correios é feita e atendida, a proxima requisição pro mesmo numero contrato mesmo com parametros diferente sera rapida e nao lenta
        List<CorreiosFranquiaContext> correiosFranquiaContexts = this.correiosFranquiaRepository.findAll();
        for(CorreiosFranquiaContext x:correiosFranquiaContexts){
            try{
                this.correiosAuth.getToken(
                        x.getNumeroContrato(),
                        x.getNumeroCartaoPostal(),
                        x.getUsuario(),
                        x.getSenha()
                );
            }catch (Exception e){
                System.out.println("Exception na gambiarra");
            }
        }
    }

}
