package com.skyler.catalogo.domain.shipping.comprimentoCaixa;

import lombok.Data;

@Data
public class ComprimentoCaixaDTO {

    private String systemId;
    private String franquiaId;
    private Float largura;//22.5
    private Float comprimento;//24.5
    private Float altura;//10.5

}
