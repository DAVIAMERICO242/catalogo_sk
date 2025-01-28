package com.skyler.catalogo.domain.pedidos;


import com.skyler.catalogo.domain.descontos.carrinho.DTOs.DescontoAplicadoDTO;
import com.skyler.catalogo.domain.descontos.carrinho.entities.Desconto;
import com.skyler.catalogo.domain.descontos.carrinho.enums.DescontoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.enums.TermoTipo;
import com.skyler.catalogo.domain.descontos.carrinho.repositories.DescontoRepository;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoVariacaoRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoVariacaoRepository produtoVariacaoRepository;
    private final DescontoRepository descontoRepository;
    private final LojaRepository lojaRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoVariacaoRepository produtoVariacaoRepository, DescontoRepository descontoRepository, LojaRepository lojaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
        this.descontoRepository = descontoRepository;
        this.lojaRepository = lojaRepository;
    }


    public void novoPedido(PedidoDTO pedidoDTO){
//        this.validarPedido(pedidoDTO);
        this.pedidoRepository.save(this.dtoToEntity(pedidoDTO));
    }

//    private void validarPedido(PedidoDTO pedidoDTO){
//        if(pedidoDTO.getDescontosAplicados().isEmpty()){
//            List<ProdutoVariacao> produtoVariacaos = this.produtoVariacaoRepository.findAllById(
//                    new HashSet<>(pedidoDTO.getVariacoesCompradas().stream().map(o->o.getSystemId()).toList())
//            );
//            Float trueValue = produtoVariacaos.stream()
//                    .map(p -> p.getProduto().getPreco()) // Extrai os preços como Float
//                    .reduce(0f, Float::sum);
//            if(Math.abs(trueValue - pedidoDTO.getValor())>0.5){
//                throw new RuntimeException("Pedido inválido!");
//            }
//        }else{
//
//        }
//    }



    private PedidoDTO entityToDto(Pedido pedido){//usar join fetch
        PedidoDTO dto = new PedidoDTO();
        dto.setSystemId(pedido.getSystemId());
        PedidoDTO.Loja loja = new PedidoDTO.Loja();
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
            PedidoDTO.Produto produtoComprado = new PedidoDTO.Produto();
            produtoComprado.setSystemId(produtoBase.getSystemId());
            produtoComprado.setNome(produtoBase.getDescricao());
            produtoComprado.setSku(produtoBase.getSku());
            produtoComprado.setValorBase(produtoBase.getPreco());
            for(ProdutoVariacao produtoVariacao:produtoBase.getVariacoes()){
                if(pedido.getProdutos().stream().anyMatch(o->o.getSystemId().equals(produtoVariacao.getSystemId()))){//esses produtos sao as variacoes
                    PedidoDTO.ProdutoVariacao produtoVariacaoDTO = new PedidoDTO.ProdutoVariacao();
                    produtoVariacaoDTO.setSystemId(produtoVariacao.getSystemId());
                    produtoVariacaoDTO.setSku(produtoVariacao.getSkuPonto());
                    produtoVariacaoDTO.setCor(produtoVariacao.getCor());
                    produtoVariacaoDTO.setTamanho(produtoVariacao.getTamanho());
                    produtoVariacaoDTO.setValorBase(produtoBase.getPreco());
                    produtoVariacaoDTO.setFotoUrl(produtoVariacao.getFotoUrl());
                    produtoComprado.addVariacaoComprada(produtoVariacaoDTO);
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


    private Pedido dtoToEntity(PedidoDTO pedidoDTO){
        Pedido pedido = new Pedido();
        if(pedidoDTO.getSystemId()!=null){
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
