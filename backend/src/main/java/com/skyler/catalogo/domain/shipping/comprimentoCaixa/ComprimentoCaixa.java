package com.skyler.catalogo.domain.shipping.comprimentoCaixa;


import com.skyler.catalogo.domain.franquias.Franquia;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(of="systemId")
@Table(name="comprimento_caixa_franquia")
public class ComprimentoCaixa {
    @Id
    private String systemId = UUID.randomUUID().toString();
    private Float largura;//22.5
    private Float comprimento;//24.5
    private Float altura;//10.5

    @ManyToOne
    @JoinColumn(name="franquia_id")
    private Franquia franquia;
}
