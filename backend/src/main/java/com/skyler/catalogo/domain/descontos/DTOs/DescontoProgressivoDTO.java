package com.skyler.catalogo.domain.descontos.DTOs;

import com.skyler.catalogo.domain.descontos.interfaces.DelimitedExcludedInterface;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DescontoProgressivoDTO implements DelimitedExcludedInterface {
    String systemId;
    List<String> delimitedCategorias = new ArrayList<>();
    List<String> excludedCategorias = new ArrayList<>();
    List<String> delimitedLinhas = new ArrayList<>();
    List<String> excludedLinhas = new ArrayList<>();
    List<String> delimitedGrupos = new ArrayList<>();
    List<String> excludedGrupos = new ArrayList<>();
    List<IntervalDescontoDTO> intervalos = new ArrayList<>();

    public void addIntervalo(IntervalDescontoDTO intervalDescontoDTO){
        this.intervalos.add(intervalDescontoDTO);
    }

    @Override
    public void addDelimitedCategoria(String termo) {
        this.delimitedCategorias.add(termo);
    }

    @Override
    public void addDelimitedLinha(String termo) {
        this.delimitedLinhas.add(termo);
    }

    @Override
    public void addDelimitedGrupo(String termo) {
        this.delimitedGrupos.add(termo);
    }

    @Override
    public void addExcudedCategoria(String termo) {
        this.excludedCategorias.add(termo);
    }

    @Override
    public void addExcludedLinha(String termo) {
        this.excludedLinhas.add(termo);
    }

    @Override
    public void addExcludedGrupo(String termo) {
        this.excludedGrupos.add(termo);
    }
}
