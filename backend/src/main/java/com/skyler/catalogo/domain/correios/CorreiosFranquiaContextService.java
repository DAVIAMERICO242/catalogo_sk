package com.skyler.catalogo.domain.correios;


import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CorreiosFranquiaContextService {

    private final CorreiosFranquiaRepository correiosFranquiaRepository;
    private final FranquiaRepository franquiaRepository;

    public CorreiosFranquiaContextService(CorreiosFranquiaRepository correiosFranquiaRepository, FranquiaRepository franquiaRepository) {
        this.correiosFranquiaRepository = correiosFranquiaRepository;
        this.franquiaRepository = franquiaRepository;
    }

    public CorreiosFranquiaDTO getForFranquia(String franquiaId){
        return this.entityToDto(this.correiosFranquiaRepository.findByFranquiaId(franquiaId).orElse(null));
    }

    public CorreiosFranquiaDTO updateOrSave(CorreiosFranquiaDTO correiosFranquiaDTO){
        return this.entityToDto(this.correiosFranquiaRepository.save(this.dtoToEntity(correiosFranquiaDTO)));
    }

    public CorreiosFranquiaDTO entityToDto(CorreiosFranquiaContext correiosFranquiaContext){
        if(correiosFranquiaContext==null){
            return null;
        }
        CorreiosFranquiaDTO output = new CorreiosFranquiaDTO();
        output.setSystemId(correiosFranquiaContext.getSystemId());
        output.setFranquiaId(correiosFranquiaContext.getFranquia().getSystemId());
        output.setUsuario(correiosFranquiaContext.getUsuario());
        output.setNumeroContrato(correiosFranquiaContext.getNumeroContrato());
        output.setNumeroDiretoriaRegional(correiosFranquiaContext.getNumeroDiretoriaRegional());
        output.setSenha(correiosFranquiaContext.getSenha());
        output.setCodigoPac(correiosFranquiaContext.getCodigoPac());
        output.setCodigoSedex(correiosFranquiaContext.getCodigoSedex());
        output.setCepOrigem(correiosFranquiaContext.getCepOrigem());
        return output;
    }

    public CorreiosFranquiaContext dtoToEntity(CorreiosFranquiaDTO correiosFranquiaDTO){
        Optional<Franquia> franquiaOptional = this.franquiaRepository.findById(correiosFranquiaDTO.getFranquiaId());
        if(franquiaOptional.isEmpty()){
            throw new RuntimeException("Franquia não encontrada");
        }
        CorreiosFranquiaContext correiosFranquiaContext = new CorreiosFranquiaContext();
        if(correiosFranquiaContext.getSystemId()!=null && !correiosFranquiaContext.getSystemId().isBlank()){
            correiosFranquiaContext.setSystemId(correiosFranquiaDTO.getSystemId());
        }
        correiosFranquiaContext.setFranquia(franquiaOptional.get());
        correiosFranquiaContext.setUsuario(correiosFranquiaDTO.getUsuario());
        correiosFranquiaContext.setNumeroContrato(correiosFranquiaDTO.getNumeroContrato());
        correiosFranquiaContext.setNumeroDiretoriaRegional(correiosFranquiaDTO.getNumeroDiretoriaRegional());
        correiosFranquiaContext.setSenha(correiosFranquiaDTO.getSenha());
        correiosFranquiaContext.setCodigoPac(correiosFranquiaDTO.getCodigoPac());
        correiosFranquiaContext.setCodigoSedex(correiosFranquiaDTO.getCodigoSedex());
        correiosFranquiaContext.setCepOrigem(correiosFranquiaDTO.getCepOrigem());
        return correiosFranquiaContext;

    }
}
