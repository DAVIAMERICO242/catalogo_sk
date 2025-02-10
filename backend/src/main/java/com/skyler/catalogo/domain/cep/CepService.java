package com.skyler.catalogo.domain.cep;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {

    public CepResponse getLocalidade(String cep){
        CepResponse output = new CepResponse();
        RestTemplate restTemplate = new RestTemplate();
        CepThirdApiResponse response = restTemplate.getForObject("https://viacep.com.br/ws/"+cep+"/json",CepThirdApiResponse.class);
        output.setBairro(response.getBairro());
        output.setRua(response.getLogradouro());
        output.setCidade(response.getLocalidade());
        output.setEstado(response.getEstado());
        return output;
    }
}
