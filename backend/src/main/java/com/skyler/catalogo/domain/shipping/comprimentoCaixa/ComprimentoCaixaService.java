package com.skyler.catalogo.domain.shipping.comprimentoCaixa;


import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import org.springframework.stereotype.Service;

@Service
public class ComprimentoCaixaService {

    private final FranquiaRepository franquiaRepository;
    private final ComprimentoCaixaRepository comprimentoCaixaRepository;

    public ComprimentoCaixaService(FranquiaRepository franquiaRepository, ComprimentoCaixaRepository comprimentoCaixaRepository) {
        this.franquiaRepository = franquiaRepository;
        this.comprimentoCaixaRepository = comprimentoCaixaRepository;
    }

    public ComprimentoCaixaDTO getByFranquiaId(String franquiaId){
        return this.entityToDto(this.comprimentoCaixaRepository.findByFranquiaId(franquiaId).orElse(null));
    }


    public ComprimentoCaixaDTO updateOrSave(ComprimentoCaixaDTO comprimentoCaixaDTO){
        return this.entityToDto(this.comprimentoCaixaRepository.save(this.dtoToEntity(comprimentoCaixaDTO)));
    }


    public ComprimentoCaixa dtoToEntity(ComprimentoCaixaDTO comprimentoCaixaDTO){
        ComprimentoCaixa comprimentoCaixa = new ComprimentoCaixa();
        if(comprimentoCaixaDTO.getSystemId()!=null && !comprimentoCaixaDTO.getSystemId().isBlank()){
            comprimentoCaixa.setSystemId(comprimentoCaixaDTO.getSystemId());
        }
        comprimentoCaixa.setFranquia(this.franquiaRepository.findById(comprimentoCaixaDTO.getFranquiaId()).get());
        comprimentoCaixa.setComprimento(comprimentoCaixaDTO.getComprimento());
        comprimentoCaixa.setAltura(comprimentoCaixaDTO.getAltura());
        comprimentoCaixa.setLargura(comprimentoCaixaDTO.getLargura());
        return comprimentoCaixa;
    }


    public ComprimentoCaixaDTO entityToDto(ComprimentoCaixa comprimentoCaixa){
        if(comprimentoCaixa==null){
            return null;
        }
        ComprimentoCaixaDTO output = new ComprimentoCaixaDTO();
        output.setSystemId(comprimentoCaixa.getSystemId());
        output.setFranquiaId(comprimentoCaixa.getFranquia().getSystemId());
        output.setComprimento(comprimentoCaixa.getComprimento());
        output.setLargura(comprimentoCaixa.getLargura());
        output.setAltura(comprimentoCaixa.getAltura());
        return output;
    }
}
