package com.skyler.catalogo.domain.shipping.correios.apiCorreios;

import com.skyler.catalogo.domain.shipping.correios.apiCorreios.apiModels.TokenAcessoResponse;
import com.skyler.catalogo.domain.shipping.correios.apiCorreios.apiModels.TokenRequestPayload;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CorreiosAuth {

    List<TokenCache> tokensCache = new ArrayList<>();
    private final Integer tokenExpirationInHours = 6;

    static class TokenCache{
        LocalDateTime expiresAt;
        String nuContrato;
        String token;
    }

    public String getToken(
            String nuContrato,
            String cartaoPostagem,
            String username,
            String password
    ){
        if(this.isTokenCacheExpiredOrNotDefined(nuContrato)){//expirado para um nuContrato especifico
            String novoToken = this.getRefreshedToken(username,password,cartaoPostagem);
            this.clearToken(nuContrato);//nao dara erro se nao foi definido
            this.assignToken(nuContrato,novoToken);
            return novoToken;
        }else{
            return this.getCachedToken(nuContrato);
        }
    }

    private String getRefreshedToken(
            String username,
            String password,
            String cartaoPostagem
            ){
        RestTemplate restTemplate = new RestTemplate();
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        TokenRequestPayload body = new TokenRequestPayload();
        body.setNumero(cartaoPostagem);
        HttpEntity<TokenRequestPayload> entity = new HttpEntity<>(body, headers);
        TokenAcessoResponse response = restTemplate.exchange("https://api.correios.com.br/token/v1/autentica/cartaopostagem", HttpMethod.GET,entity,TokenAcessoResponse.class).getBody();
        return response.getToken();
    }

    private void assignToken(String nuContrato, String token){
        TokenCache tokenCache = new TokenCache();
        tokenCache.nuContrato = nuContrato;
        tokenCache.token = token;
        tokenCache.expiresAt = LocalDateTime.now().plusHours(this.tokenExpirationInHours);
        this.tokensCache.add(tokenCache);
    }

    private String getCachedToken(String nuContrato){
        return this.tokensCache.stream().filter(o->o.nuContrato.equals(nuContrato)).findFirst().get().token;
    }

    private Boolean isTokenCacheExpiredOrNotDefined(String nuContrato){
        Optional<TokenCache> tokenCacheOptional = this.tokensCache.stream().filter(o->o.nuContrato.equals(nuContrato)).findFirst();
        if(tokenCacheOptional.isEmpty()){
            return true;
        }else{
            return tokenCacheOptional.get().expiresAt.isAfter(LocalDateTime.now());
        }
    }

    private void clearToken(
            String nuContrato
    ){//evitar vazamento de memoria, mas nunca vai acontecermesmo se nao tivesse implementado isso
        this.tokensCache = this.tokensCache.stream().filter(o->!o.nuContrato.equals(nuContrato)).collect(Collectors.toList());
    }
}
