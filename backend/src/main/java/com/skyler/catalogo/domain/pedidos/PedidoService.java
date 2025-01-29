package com.skyler.catalogo.domain.pedidos;


import com.skyler.catalogo.domain.descontos.DTOs.AfterAppliedChain;
import com.skyler.catalogo.domain.descontos.DTOs.DescontoAplicadoDTO;
import com.skyler.catalogo.domain.descontos.entities.Desconto;
import com.skyler.catalogo.domain.descontos.repositories.DescontoRepository;
import com.skyler.catalogo.domain.descontos.services.DescontoService;
import com.skyler.catalogo.domain.descontos.services.DiscountCalculator;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.domain.pedidos.DTOs.*;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoVariacaoRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoVariacaoRepository produtoVariacaoRepository;
    private final DescontoRepository descontoRepository;
    private final LojaRepository lojaRepository;
    private final DiscountCalculator discountCalculator;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoVariacaoRepository produtoVariacaoRepository, DescontoRepository descontoRepository, LojaRepository lojaRepository, DiscountCalculator discountCalculator) {
        this.pedidoRepository = pedidoRepository;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
        this.descontoRepository = descontoRepository;
        this.lojaRepository = lojaRepository;
        this.discountCalculator = discountCalculator;
    }

    public List<PedidoAfterCalculationsDTO> getPedidos(String lojaSystemId){
        List<PedidoAfterCalculationsDTO> output = new ArrayList<>();
        List<Pedido> pedidos = this.pedidoRepository.findAllByLojaId(lojaSystemId);
        for(Pedido pedido:pedidos){
            output.add(this.entityToDto(pedido));
        }
        return output;
    }

    public void novoPedido(PedidoBeforeCalculationsDTO pedidoSemValores){
        PedidoAfterCalculationsDTO mature = this.getPedidoMature(pedidoSemValores);
        this.pedidoRepository.save(this.dtoToEntity(mature));
    }




    private PedidoAfterCalculationsDTO getPedidoMature(PedidoBeforeCalculationsDTO pedidoBeforeCalculationsDTO){
        PedidoAfterCalculationsDTO output = new PedidoAfterCalculationsDTO();
        LojaPedidoDTO lojaPedidoDTO = new LojaPedidoDTO();
        lojaPedidoDTO.setNome(pedidoBeforeCalculationsDTO.getLoja().getNome());
        lojaPedidoDTO.setSystemId(pedidoBeforeCalculationsDTO.getLoja().getSystemId());
        lojaPedidoDTO.setSlug(pedidoBeforeCalculationsDTO.getLoja().getSlug());
        output.setLoja(lojaPedidoDTO);
        output.setDocumento(pedidoBeforeCalculationsDTO.getDocumento());
        output.setNome(pedidoBeforeCalculationsDTO.getNome());
        output.setNumero(pedidoBeforeCalculationsDTO.getNumero());
        output.setRua(pedidoBeforeCalculationsDTO.getRua());
        output.setBairro(pedidoBeforeCalculationsDTO.getBairro());
        output.setCidade(pedidoBeforeCalculationsDTO.getCidade());
        output.setEstado(pedidoBeforeCalculationsDTO.getEstado());
        output.setCep(pedidoBeforeCalculationsDTO.getCep());
        output.setTelefone(pedidoBeforeCalculationsDTO.getTelefone());
        output.setValorFrete(pedidoBeforeCalculationsDTO.getValorFrete());
        output.setPago(false);
        output.setProdutos(pedidoBeforeCalculationsDTO.getProdutos());
        AfterAppliedChain after = this.discountCalculator.processChainForCurrentEpochAndDiscountable(pedidoBeforeCalculationsDTO);
        output.setDescontosAplicados(after.getDescontos());
        output.setValor(after.getValorFinal());
        return output;

    }


    private PedidoAfterCalculationsDTO entityToDto(Pedido pedido){//usar join fetch
        PedidoAfterCalculationsDTO dto = new PedidoAfterCalculationsDTO();
        dto.setSystemId(pedido.getSystemId());
        LojaPedidoDTO loja = new LojaPedidoDTO();
        loja.setSystemId(pedido.getLoja().getSystemId());
        loja.setNome(pedido.getLoja().getNome());
        loja.setSlug(pedido.getLoja().getSlug());
        dto.setLoja(loja);
        dto.setMoment(pedido.getMoment());
        dto.setDocumento(pedido.getDocumento());
        dto.setNome(pedido.getNome());
        dto.setNumero(pedido.getNumero());
        dto.setRua(pedido.getRua());
        dto.setBairro(pedido.getBairro());
        dto.setCidade(pedido.getCidade());
        dto.setEstado(pedido.getEstado());
        dto.setCep(pedido.getCep());
        dto.setTelefone(pedido.getTelefone());
        dto.setValor(pedido.getValor());
        dto.setValorFrete(pedido.getValorFrete());
        dto.setPago(pedido.getPago());
        List<Produto> produtosBase = new HashSet<>(pedido.getProdutos().stream().map(o->o.getProduto()).toList()).stream().toList();
        for(Produto produtoBase:produtosBase){
            ProdutoPedidoDTO produtoComprado = new ProdutoPedidoDTO();
            produtoComprado.setSystemId(produtoBase.getSystemId());
            produtoComprado.setNome(produtoBase.getDescricao());
            produtoComprado.setSku(produtoBase.getSku());
            produtoComprado.setValorBase(produtoBase.getPreco());
            for(ProdutoVariacao produtoVariacao:produtoBase.getVariacoes()){
                if(pedido.getProdutos().stream().anyMatch(o->o.getSystemId().equals(produtoVariacao.getSystemId()))){//esses produtos sao as variacoes
                    ProdutoPedidoDTO.ProdutoVariacao produtoVariacaoDTO = new ProdutoPedidoDTO.ProdutoVariacao();
                    produtoVariacaoDTO.setSystemId(produtoVariacao.getSystemId());
                    produtoVariacaoDTO.setSku(produtoVariacao.getSkuPonto());
                    produtoVariacaoDTO.setCor(produtoVariacao.getCor());
                    produtoVariacaoDTO.setTamanho(produtoVariacao.getTamanho());
                    produtoVariacaoDTO.setValorBase(produtoBase.getPreco());
                    produtoVariacaoDTO.setFotoUrl(produtoVariacao.getFotoUrl());
                    produtoComprado.addVariacao(produtoVariacaoDTO);
                }
            }
            dto.addProdutoComprado(produtoComprado);
        }
        for(DescontosAplicados descontosAplicados:pedido.getDescontosAplicados()){
            DescontoAplicadoDTO desconto = new DescontoAplicadoDTO();
            desconto.setSystemId(descontosAplicados.getDesconto().getSystemId());
            desconto.setTipo(descontosAplicados.getDesconto().getDescontoTipo());
            desconto.setNome(descontosAplicados.getDesconto().getDiscountName());
            desconto.setValorAplicado(descontosAplicados.getAppliedValue());
            dto.addDescontoAplicado(desconto);
        }

        return dto;
    }


    private Pedido dtoToEntity(PedidoAfterCalculationsDTO pedidoDTO){
        Pedido pedido = new Pedido();
        if(pedidoDTO.getSystemId()!=null && !pedidoDTO.getSystemId().isBlank()){
            Optional<Pedido> pedidoOptional = this.pedidoRepository.findById(pedidoDTO.getSystemId());
            if(pedidoOptional.isPresent()){
                pedido = pedidoOptional.get();
            }
        }
        Optional<Loja> lojaOptional = this.lojaRepository.findById(pedidoDTO.getLoja().getSystemId());
        if(lojaOptional.isEmpty()){
            throw new RuntimeException("Loja não encontrada!");
        }
        pedido.setLoja(lojaOptional.get());
        pedido.setDocumento(pedidoDTO.getDocumento());
        pedido.setNome(pedidoDTO.getNome());
        pedido.setNumero(pedidoDTO.getNumero());
        pedido.setRua(pedidoDTO.getRua());
        pedido.setBairro(pedidoDTO.getBairro());
        pedido.setCidade(pedidoDTO.getCidade());
        pedido.setEstado(pedidoDTO.getEstado());
        pedido.setCep(pedidoDTO.getCep());
        pedido.setTelefone(pedidoDTO.getTelefone());
        pedido.setValor(pedidoDTO.getValor());
        pedido.setValorFrete(pedidoDTO.getValorFrete());
        Set<String> variacoesIds = new HashSet<>(pedidoDTO.getProdutos().stream()
                .flatMap(produto -> produto.getVariacoesCompradas().stream())
                .map(o->o.getSystemId())
                .collect(Collectors.toList()));
        List<ProdutoVariacao> variations = this.produtoVariacaoRepository.findAllById(variacoesIds);
        pedido.setProdutos(new HashSet<>(variations));
        if(pedido.getDescontosAplicados().isEmpty()){
            for(DescontoAplicadoDTO desconto:pedidoDTO.getDescontosAplicados()){
                if(desconto.getSystemId()!=null && !desconto.getSystemId().isBlank()){

                }
                Optional<Desconto> desconto1 = this.descontoRepository.findById(desconto.getSystemId());
                if(desconto1.isPresent()){
                    Desconto descontoEnt = desconto1.get();
                    DescontosAplicados descontosAplicados = new DescontosAplicados();
                    descontosAplicados.setAppliedValue(desconto.getValorAplicado());
                    descontosAplicados.setDesconto(descontoEnt);
                    pedido.addDescontoAplicado(descontosAplicados);
                }


            }
        }
        return pedido;
    }
}
