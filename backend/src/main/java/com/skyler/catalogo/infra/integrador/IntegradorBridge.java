package com.skyler.catalogo.infra.integrador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyler.catalogo.infra.integrador.DTOs.FranquiaIntegrador;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class IntegradorBridge extends IntegradorContext {
    private final RestTemplate restTemplate;

    public IntegradorBridge() {
        this.restTemplate = new RestTemplate();

    }

    public List<FranquiaIntegrador> getFranquiasAndLojas(){
        FranquiaIntegrador[] output = restTemplate.getForObject(this.integradorUrl + "/franquias",FranquiaIntegrador[].class);
        return Arrays.stream(output).toList();
    }


}
