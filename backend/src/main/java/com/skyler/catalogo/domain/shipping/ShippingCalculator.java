package com.skyler.catalogo.domain.shipping;


import com.skyler.catalogo.domain.pedidos.DTOs.ProdutoPedidoDTO;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoVariacaoRepository;
import com.skyler.catalogo.domain.shipping.comprimentoCaixa.ComprimentoCaixa;
import com.skyler.catalogo.domain.shipping.comprimentoCaixa.ComprimentoCaixaRepository;
import com.skyler.catalogo.domain.shipping.correios.CorreiosFranquiaContext;
import com.skyler.catalogo.domain.shipping.correios.CorreiosFranquiaRepository;
import com.skyler.catalogo.domain.shipping.correios.apiCorreios.CorreiosAuth;
import com.skyler.catalogo.domain.shipping.correios.apiCorreios.CorreiosBridge;
import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.domain.pedidos.DTOs.PedidoBeforeCalculationsDTO;
import com.skyler.catalogo.domain.shipping.pesoCategorias.PesoCategorias;
import com.skyler.catalogo.domain.shipping.pesoCategorias.PesoCategoriasRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShippingCalculator {

    private final CorreiosFranquiaRepository correiosFranquiaRepository;
    private final CorreiosBridge correiosBridge;
    private final CorreiosAuth correiosAuth;
    private final LojaRepository lojaRepository;
    private final ComprimentoCaixaRepository comprimentoCaixaRepository;
    private final PesoCategoriasRepository pesoCategoriasRepository;
    private final ProdutoVariacaoRepository produtoVariacaoRepository;

    public ShippingCalculator(CorreiosFranquiaRepository correiosFranquiaRepository, CorreiosBridge correiosBridge, CorreiosAuth correiosAuth, LojaRepository lojaRepository, ComprimentoCaixaRepository comprimentoCaixaRepository, PesoCategoriasRepository pesoCategoriasRepository, ProdutoVariacaoRepository produtoVariacaoRepository) {
        this.correiosFranquiaRepository = correiosFranquiaRepository;
        this.correiosBridge = correiosBridge;
        this.correiosAuth = correiosAuth;
        this.lojaRepository = lojaRepository;
        this.comprimentoCaixaRepository = comprimentoCaixaRepository;
        this.pesoCategoriasRepository = pesoCategoriasRepository;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
    }



    public FreteResponseDTO getFreteCorreios(
            ShippingCalculationRequest shippingCalculationRequest
    ){
        Franquia franquia = this.lojaRepository.findById(shippingCalculationRequest.getLojaId()).get().getFranquia();
        Optional<CorreiosFranquiaContext> correiosFranquiaContextOptional = this.correiosFranquiaRepository.findByFranquiaId(franquia.getSystemId());
        if(correiosFranquiaContextOptional.isEmpty()){
            throw new RuntimeException("Não foi possível calcular o frete, integração com correios não definida");
        }
        Optional<ComprimentoCaixa> comprimentoCaixaOptional = this.comprimentoCaixaRepository.findByFranquiaId(franquia.getSystemId());
        if(comprimentoCaixaOptional.isEmpty()){
            throw new RuntimeException("Não foi possível calcular o frete, tamanho da caixa não definido");
        }
        ComprimentoCaixa comprimentoCaixa = comprimentoCaixaOptional.get();
        CorreiosFranquiaContext correiosFranquiaContext = correiosFranquiaContextOptional.get();
        String token = this.correiosAuth.getToken(
                correiosFranquiaContext.getNumeroContrato(),
                correiosFranquiaContext.getNumeroCartaoPostal(),
                correiosFranquiaContext.getUsuario(),
                correiosFranquiaContext.getSenha()
        );
        List<PesoCategorias> pesoCategorias = this.pesoCategoriasRepository.findAllByFranquiaId(franquia.getSystemId());
        Float peso = 0f;
        List<ProdutoPedidoDTO.ProdutoVariacao> variacoesCompradas = shippingCalculationRequest.getProdutos().stream().flatMap(o->o.getVariacoesCompradas().stream()).toList();
        HashSet<String> variationsIds = new HashSet<>(
                variacoesCompradas.stream()
                        .map(o -> o.getSystemId())
                        .collect(Collectors.toSet()) // Retorna um Set genérico
        );
        List<ProdutoVariacao> variacoesCompradasAsUniqueEntities = this.produtoVariacaoRepository.findAllById(variationsIds);
        for(ProdutoPedidoDTO.ProdutoVariacao produtoVariacao:variacoesCompradas){
            Optional<ProdutoVariacao> variacaoEntityOptional = variacoesCompradasAsUniqueEntities.stream().filter(o->o.getSystemId().equals(produtoVariacao.getSystemId())).findFirst();
            if(variacaoEntityOptional.isEmpty()){
                continue;
            }
            Optional<PesoCategorias> pesoCategoriasOptional = pesoCategorias.stream().filter(o->o.getCategoria().equals(variacaoEntityOptional.get().getProduto().getCategoria())).findFirst();
            if(pesoCategoriasOptional.isEmpty()){
                continue;
            }
            Float pesoVariacao = pesoCategoriasOptional.get().getPesoGramas();
            peso = peso + pesoVariacao;
        }
        Float valorFrete = this.correiosBridge.getPrecoFrete(
                token,
                correiosFranquiaContext.getCodigoPac(),
                correiosFranquiaContext.getNumeroContrato(),
                correiosFranquiaContext.getNumeroDiretoriaRegional(),
                correiosFranquiaContext.getCepOrigem(),
                shippingCalculationRequest.getCep(),
                peso==0f?500f:peso,
                comprimentoCaixa.getComprimento(),
                comprimentoCaixa.getAltura(),
                comprimentoCaixa.getLargura()
            );
        Integer prazoFrete = this.correiosBridge.getPrazoFrete(
                token,
                correiosFranquiaContext.getCodigoPac(),
                correiosFranquiaContext.getNumeroContrato(),
                correiosFranquiaContext.getNumeroDiretoriaRegional(),
                correiosFranquiaContext.getCepOrigem(),
                shippingCalculationRequest.getCep(),
                peso==0f?500f:peso,
                comprimentoCaixa.getComprimento(),
                comprimentoCaixa.getAltura(),
                comprimentoCaixa.getLargura()
        );

        return new FreteResponseDTO(
                valorFrete,
                prazoFrete
        );

    }
}
