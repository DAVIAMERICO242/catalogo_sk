package com.skyler.catalogo.domain.pesoCategorias;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.franquias.FranquiaRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PesoCategoriasService {
    //nunca leiam aquele livro de quem mora no alto

    private final FranquiaRepository franquiaRepository;
    private final PesoCategoriasRepository pesoCategoriasRepository;

    public PesoCategoriasService(FranquiaRepository franquiaRepository, PesoCategoriasRepository pesoCategoriasRepository) {
        this.franquiaRepository = franquiaRepository;
        this.pesoCategoriasRepository = pesoCategoriasRepository;
    }

    public PesoCategoriasDTO cadastrarAtualizar(PesoCategoriasDTO pesoCategoriasRequest){
        PesoCategorias pesoCategorias = new PesoCategorias();
        if(pesoCategoriasRequest.getSystemId()!=null && !pesoCategoriasRequest.getSystemId().isBlank()){
            Optional<PesoCategorias> pesoCategoriasOptional = this.pesoCategoriasRepository.findById(pesoCategoriasRequest.getSystemId());
            if(pesoCategoriasOptional.isPresent()){
                pesoCategorias = pesoCategoriasOptional.get();
            }
        }
        Optional<PesoCategorias> verificarCategoriaPesada = this.pesoCategoriasRepository.findByCategoriaAndFranquiaId(
                pesoCategoriasRequest.getCategoria(),
                pesoCategoriasRequest.getFranquiaId()
        );
        if(verificarCategoriaPesada.isPresent() && !verificarCategoriaPesada.get().getSystemId().equals(pesoCategorias.getSystemId())){
            throw new RuntimeException("Categoria já pesada");
        }else{
            Franquia franquia = this.franquiaRepository.findById(pesoCategoriasRequest.getFranquiaId()).get();
            pesoCategorias.setCategoria(pesoCategoriasRequest.getCategoria());
            pesoCategorias.setFranquia(franquia);
            pesoCategorias.setPesoGramas(pesoCategoriasRequest.getPesoGramas());
            this.pesoCategoriasRepository.save(pesoCategorias);
        }
        return this.entityToDto(pesoCategorias);
    }

    public List<PesoCategoriasDTO> getPesos(String franquiaId){
        List<PesoCategoriasDTO> output = new ArrayList<>();
        List<PesoCategorias> entities = this.pesoCategoriasRepository.findAllByFranquiaId(franquiaId);
        for(PesoCategorias entity:entities){
            output.add(this.entityToDto(entity));
        }
        return output;
    }

    public PesoCategoriasDTO entityToDto(PesoCategorias pesoCategorias){
        PesoCategoriasDTO output = new PesoCategoriasDTO();
        output.setSystemId(pesoCategorias.getSystemId());
        output.setFranquiaId(pesoCategorias.getFranquia().getSystemId());
        output.setCategoria(pesoCategorias.getCategoria());
        output.setPesoGramas(pesoCategorias.getPesoGramas());
        return output;
    }

    public PesoCategorias dtoToEntity(PesoCategoriasDTO pesoCategoriasDTO){
        Optional<Franquia> franquiaOptional = this.franquiaRepository.findById(pesoCategoriasDTO.getSystemId());
        if(franquiaOptional.isEmpty()){
            throw new RuntimeException("FRANQUIA NAO ENCONTRADA");
        }
        PesoCategorias pesoCategorias = new PesoCategorias();
        if(pesoCategoriasDTO.getSystemId()!=null && !pesoCategoriasDTO.getSystemId().isBlank()){
            pesoCategorias.setSystemId(pesoCategoriasDTO.getSystemId());
        }
        pesoCategorias.setFranquia(franquiaOptional.get());
        pesoCategorias.setCategoria(pesoCategoriasDTO.getCategoria());
        pesoCategorias.setPesoGramas(pesoCategoriasDTO.getPesoGramas());
        return pesoCategorias;
    }
}
