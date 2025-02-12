package com.skyler.catalogo.domain.shipping.faixasCep;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShippingRulesService {


    private final FranquiaRepository franquiaRepository;
    private final ShippingRulesRepository shippingRulesRepository;

    public ShippingRulesService(FranquiaRepository franquiaRepository, ShippingRulesRepository shippingRulesRepository) {
        this.franquiaRepository = franquiaRepository;
        this.shippingRulesRepository = shippingRulesRepository;
    }

    public List<ShippingRuleDTO> getRules(String franquiaId){
        List<ShippingRuleDTO> output = new ArrayList<>();
        List<ShippingRules> shippingRules = this.shippingRulesRepository.findAllByFranquiaId(franquiaId);
        for(ShippingRules shippingRule:shippingRules){
            output.add(this.entityToDTO(shippingRule));
        }
        return output;
    }

    public ShippingRuleDTO updateOrSave(ShippingRuleDTO shippingRuleDTO){
        ShippingRules shippingRule = this.dtoToEntity(shippingRuleDTO);
        return this.entityToDTO(this.shippingRulesRepository.save(shippingRule));
    }

    public ShippingRules dtoToEntity(ShippingRuleDTO shippingRuleDTO){
        Optional<Franquia> franquiaOptional = this.franquiaRepository.findById(shippingRuleDTO.getFranquiaId());
        if(franquiaOptional.isEmpty()){
            throw new RuntimeException("Franquia não encontrada");
        }
        ShippingRules output = new ShippingRules();
        if(shippingRuleDTO.getSystemId()!=null && !shippingRuleDTO.getSystemId().isBlank()){
            output.setSystemId(shippingRuleDTO.getSystemId());
        }
        output.setFranquia(franquiaOptional.get());
        output.setCepInicio(shippingRuleDTO.getCepInicio());
        output.setCepFim(shippingRuleDTO.getCepFim());
        output.setMinValueToApply(shippingRuleDTO.getMinValueToApply());
        output.setValorFixo(shippingRuleDTO.getValorFixo());
        return output;
    }

    public ShippingRuleDTO entityToDTO(ShippingRules shippingRules){
        ShippingRuleDTO output = new ShippingRuleDTO();
        output.setSystemId(shippingRules.getSystemId());
        output.setFranquiaId(shippingRules.getFranquia().getSystemId());
        output.setCepInicio(shippingRules.getCepInicio());
        output.setCepFim(shippingRules.getCepFim());
        output.setMinValueToApply(shippingRules.getMinValueToApply());
        output.setValorFixo(shippingRules.getValorFixo());
        return output;
    }
}
