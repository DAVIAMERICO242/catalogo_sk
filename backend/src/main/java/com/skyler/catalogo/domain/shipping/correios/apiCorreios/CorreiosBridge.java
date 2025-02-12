package com.skyler.catalogo.domain.shipping.correios.apiCorreios;


import com.skyler.catalogo.domain.shipping.correios.apiCorreios.apiModels.PrazoFreteResponse;
import com.skyler.catalogo.domain.shipping.correios.apiCorreios.apiModels.PrecoFreteResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CorreiosBridge {//BY DAVI AMERICO SANCHES XDDDDDDDDDDDDDDDDDD


    public Integer getPrazoFrete(
            String token,
            String pacOuSedexCodigo,
            String nuContrato,
            String nuDR,
            String cepOrigem,
            String cepDestino,
            Integer pesoEmGramas,
            Integer comprimento,
            Integer altura,
            Integer largura
    ){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        String url = "https://api.correios.com.br/prazo/v1/nacional/"
                + pacOuSedexCodigo
                + "?nuContrato=" + nuContrato
                + "&nuDR=" + nuDR
                + "&cepOrigem=" + cepOrigem
                + "&cepDestino=" + cepDestino
                + "&psObjeto=" + pesoEmGramas
                + "&comprimento="+comprimento
                + "&altura="+altura
                + "&largura="+largura
                + "&tpObjeto=2";
        PrazoFreteResponse response  = restTemplate.exchange(
                url,
                HttpMethod.GET,httpEntity,
                PrazoFreteResponse.class
        ).getBody();
        return response.getPrazoEntrega() + 1;//prazo máximo atualmente nos correios
    }

    public Float getPrecoFrete(
            String token,
            String pacOuSedexCodigo,
            String nuContrato,
            String nuDR,
            String cepOrigem,
            String cepDestino,
            Integer pesoEmGramas,
            Integer comprimento,
            Integer altura,
            Integer largura
    ){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        String url = "https://api.correios.com.br/preco/v1/nacional/"
                + pacOuSedexCodigo
                + "?nuContrato=" + nuContrato
                + "&nuDR=" + nuDR
                + "&cepOrigem=" + cepOrigem
                + "&cepDestino=" + cepDestino
                + "&psObjeto=" + pesoEmGramas
                + "&comprimento="+comprimento
                + "&altura="+altura
                + "&largura="+largura
                + "&tpObjeto=2";
        PrecoFreteResponse response  = restTemplate.exchange(
                url,
                HttpMethod.GET,httpEntity,
                PrecoFreteResponse.class
        ).getBody();
        return Float.parseFloat(response.getPcFinal().replace(",","."));//prazo máximo atualmente nos correios
    }
}
