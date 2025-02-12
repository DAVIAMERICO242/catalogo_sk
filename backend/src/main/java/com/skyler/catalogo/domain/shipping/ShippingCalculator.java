package com.skyler.catalogo.domain.shipping;


import com.skyler.catalogo.domain.descontos.interfaces.Discountable;
import com.skyler.catalogo.domain.descontos.services.DiscountCalculator;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.pedidos.DTOs.LojaPedidoDTO;
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
import com.skyler.catalogo.domain.shipping.faixasCep.ShippingRules;
import com.skyler.catalogo.domain.shipping.faixasCep.ShippingRulesRepository;
import com.skyler.catalogo.domain.shipping.pesoCategorias.PesoCategorias;
import com.skyler.catalogo.domain.shipping.pesoCategorias.PesoCategoriasRepository;
import lombok.Getter;
import lombok.Setter;
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
    private final ShippingRulesRepository faixaCepRepository;
    private final DiscountCalculator discountCalculator;

    public ShippingCalculator(CorreiosFranquiaRepository correiosFranquiaRepository, CorreiosBridge correiosBridge, CorreiosAuth correiosAuth, LojaRepository lojaRepository, ComprimentoCaixaRepository comprimentoCaixaRepository, PesoCategoriasRepository pesoCategoriasRepository, ProdutoVariacaoRepository produtoVariacaoRepository, ShippingRulesRepository faixaCepRepository, DiscountCalculator discountCalculator) {
        this.correiosFranquiaRepository = correiosFranquiaRepository;
        this.correiosBridge = correiosBridge;
        this.correiosAuth = correiosAuth;
        this.lojaRepository = lojaRepository;
        this.comprimentoCaixaRepository = comprimentoCaixaRepository;
        this.pesoCategoriasRepository = pesoCategoriasRepository;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
        this.faixaCepRepository = faixaCepRepository;
        this.discountCalculator = discountCalculator;
    }

    public HowShouldBeCalculatedResponse getHowShouldBeCalculated(ShippingCalculationRequest shippingCalculationRequest){
        Loja loja = this.lojaRepository.findById(shippingCalculationRequest.getLojaId()).get();
        Franquia franquia = loja.getFranquia();
        List<ShippingRules> faixasCep = this.faixaCepRepository.findAllByFranquiaId(franquia.getSystemId());
        for(ShippingRules faixaCep:faixasCep){
            if(this.isCepBetween(shippingCalculationRequest.getCep(),faixaCep.getCepInicio(),faixaCep.getCepFim())){
                return new HowShouldBeCalculatedResponse(true);
            }
        }
        return new HowShouldBeCalculatedResponse(false);
    }

    public FreteResponseDTO getFrete(ShippingCalculationRequest shippingCalculationRequest){//precisa do valor maturo aaaa
        Loja loja = this.lojaRepository.findById(shippingCalculationRequest.getLojaId()).get();
        Franquia franquia = loja.getFranquia();
        List<ShippingRules> faixasCep = this.faixaCepRepository.findAllByFranquiaId(franquia.getSystemId());
        List<ShippingRules> faixasCepPrecificadas = faixasCep.stream().filter(o->o.getMinValueToApply()>0).toList();
        List<ShippingRules> faixasCepNaoPrecificadas = faixasCep.stream().filter(o->o.getMinValueToApply()>0).toList();
        ShippingDiscountable shippingDiscountable = this.mapToDiscountable(shippingCalculationRequest,loja);
        Float valorCarrinhoComDescontos = this.discountCalculator.processChainForCurrentEpochAndDiscountable(shippingDiscountable).getValorFinal();
        for(ShippingRules faixaCepPrecificada:faixasCepPrecificadas){
            if(valorCarrinhoComDescontos>=faixaCepPrecificada.getMinValueToApply() && this.isCepBetween(shippingCalculationRequest.getCep(),faixaCepPrecificada.getCepInicio(),faixaCepPrecificada.getCepFim())){
                return new FreteResponseDTO(
                  faixaCepPrecificada.getValorFixo(),
                  faixaCepPrecificada.getPrazo()
                );
            }
        }
        for(ShippingRules faixaCepNaoPrecificada:faixasCepNaoPrecificadas){
            if(this.isCepBetween(shippingCalculationRequest.getCep(),faixaCepNaoPrecificada.getCepInicio(),faixaCepNaoPrecificada.getCepFim())){
                return new FreteResponseDTO(
                        faixaCepNaoPrecificada.getValorFixo(),
                        faixaCepNaoPrecificada.getPrazo()
                );
            }
        }
        return this.getFreteCorreios(shippingCalculationRequest,franquia);
    }


    public FreteResponseDTO getFreteCorreios(
            ShippingCalculationRequest shippingCalculationRequest
            ,Franquia franquia
    ){
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
        Float pesoCubado = this.getPesoCubado(
                comprimentoCaixa.getComprimento()
                ,comprimentoCaixa.getAltura()
                ,comprimentoCaixa.getAltura()
        );
        peso = (pesoCubado>peso)?pesoCubado:peso;
        String pacSedexCodigo = (shippingCalculationRequest.getPacSedex().equals(PacSedexEnum.PAC))?correiosFranquiaContext.getCodigoPac():correiosFranquiaContext.getCodigoSedex();
        Float valorFrete = this.correiosBridge.getPrecoFrete(
                token,
                pacSedexCodigo,
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
                pacSedexCodigo,
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

    private ShippingDiscountable mapToDiscountable(
            ShippingCalculationRequest shippingCalculationRequest,
            Loja lojaEntity
    ){//calcular o valor minimo do pedido para as faixas de cep, preciso aplicar os possiveis descontos
        ShippingDiscountable output = new ShippingDiscountable();
        LojaPedidoDTO lojaPedidoDTO = new LojaPedidoDTO();
        lojaPedidoDTO.setSystemId(lojaEntity.getSystemId());
        lojaPedidoDTO.setNome(lojaEntity.getNome());
        lojaPedidoDTO.setSlug(lojaEntity.getSlug());
        output.setLoja(lojaPedidoDTO);
        output.setProdutos(shippingCalculationRequest.getProdutos());
        return output;
    }

    private Float getPesoCubado(
            Float comprimento,
            Float altura,
            Float largura
    ){
        // Cálculo do peso cubado com conversão para float para garantir precisão
        float pesoCubado = (largura * comprimento * altura) / 6000f;

        // Arredondamento para 2 casas decimais
        return Math.round(pesoCubado * 100.0) / 100.0f;
    }

    private Boolean isCepBetween(String cep,String cepInferior,String cepSuperior){
        return cep.compareTo(cepInferior) >= 0 && cep.compareTo(cepSuperior) <=0;
    }
}
